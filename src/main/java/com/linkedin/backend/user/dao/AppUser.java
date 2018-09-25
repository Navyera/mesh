package com.linkedin.backend.user.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linkedin.backend.connection.Connection;
import com.linkedin.backend.dto.*;
import com.linkedin.backend.post.Comment;
import com.linkedin.backend.post.Post;
import org.apache.commons.collections4.ListUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "User")
@Table(name = "user")
public class AppUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String role;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private List<Skill> skills;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "user_likes_post",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    @JsonManagedReference
    private List<Post> likedPosts;


    @OneToMany(
            mappedBy = "requester",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Connection> requestedConnections;

    @OneToMany(
            mappedBy = "receiver",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Connection> receivedConnections;

    public AppUser() {
    }

    public AppUser(Profile profile) {
        this.profile = profile;
        profile.setUser(this);
    }

    public AppUser(String email, String password, String firstName, String lastName, String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = "ROLE_USER";
        this.profile = new Profile();
        profile.setUser(this);
    }

    public void addPost(Post post) {
        post.setUser(this);
        posts.add(post);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Role> getAuthority() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(role));
        return roles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkillsFromStrList(List<String> skills) {
        this.skills = skills.stream().map(skill -> new Skill(skill, this)).collect(Collectors.toList());
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public UserDetailsDTO toUserDetails() {
        return new UserDetailsDTO(this.firstName, this.lastName, this.phone,"", this.email);
    }

    public ProfileDTO toProfileDTO() {
        return new ProfileDTO(firstName, lastName, profile.getAbout(), profile.getEducation(), profile.getJob(), skills);
    }

    public ProfileViewDTO toProfileViewDTO() {
        return new ProfileViewDTO(toProfileDTO(), new PermissionsDTO(getProfile().getPermissions()));
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public void likePost(Post post) {
        likedPosts.add(post);
    }

    public void commentPost(String body, Post post) {
        Comment comment = new Comment(this, post, body);

        comments.add(comment);
    }

    public List<Connection> getRequestedConnections() {
        return requestedConnections;
    }

    public void setRequestedConnections(List<Connection> requestedConnections) {
        this.requestedConnections = requestedConnections;
    }

    public List<Connection> getReceivedConnections() {
        return receivedConnections;
    }

    public void setReceivedConnections(List<Connection> receivedConnections) {
        this.receivedConnections = receivedConnections;
    }

    public void addConnection(AppUser user) {
        Connection connection = new Connection(this, user);
        requestedConnections.add(connection);
    }

    public List<Integer> getFriendIDs() {
        List<Integer> activeReceived = receivedConnections.stream().filter(c -> c.getAccepted() == 1)
                                                                   .map(c -> c.getRequester().getId())
                                                                   .collect(Collectors.toList());

        List<Integer> activeRequested = requestedConnections.stream().filter(c -> c.getAccepted() == 1)
                                                                     .map(c -> c.getReceiver().getId())
                                                                     .collect(Collectors.toList());
        return ListUtils.union(activeReceived, activeRequested);
    }

    public List<PostDTO> getUserFeed() {
        List<PostDTO> postListA = receivedConnections.stream()
                                                     .filter(c -> c.getAccepted() == 1)
                                                     .map(c -> c.getRequester().getPosts())
                                                     .flatMap(List::stream)
                                                     .map(PostDTO::new)
                                                     .collect(Collectors.toList());

        List<PostDTO> postListB = receivedConnections.stream()
                                                     .filter(c -> c.getAccepted() == 1)
                                                     .map(c -> c.getRequester().getPosts())
                                                     .flatMap(List::stream)
                                                     .map(PostDTO::new)
                                                     .collect(Collectors.toList());

        List<PostDTO> otherPosts = ListUtils.union(postListA, postListB);

        List<PostDTO> myPosts = getPosts().stream()
                                          .map(PostDTO::new)
                                          .collect(Collectors.toList());

        return ListUtils.union(otherPosts, myPosts);
    }
}
