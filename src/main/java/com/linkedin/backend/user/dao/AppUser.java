package com.linkedin.backend.user.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linkedin.backend.connection.Connection;
import com.linkedin.backend.dto.*;
import com.linkedin.backend.post.Comment;
import com.linkedin.backend.post.Like;
import com.linkedin.backend.post.Post;
import org.apache.commons.collections4.ListUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Like> likes;

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

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
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
        return likes.stream().map(Like::getPost).collect(Collectors.toList());
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

    public List<Post> getRelevantPosts() {
        Set<Post> set = new LinkedHashSet<>();

        set.addAll(likes.stream().map(Like::getPost).collect(Collectors.toList()));
        set.addAll(posts);

        return new ArrayList<>(set);
    }

    public List<Post> getUserFeed() {
        List<Post> postListA = receivedConnections.stream()
                                                     .filter(c -> c.getAccepted() == 1)
                                                     .map(c -> c.getRequester().getRelevantPosts())
                                                     .flatMap(List::stream)
                                                     .collect(Collectors.toList());

        List<Post> postListB = requestedConnections.stream()
                                                     .filter(c -> c.getAccepted() == 1)
                                                     .map(c -> c.getReceiver().getRelevantPosts())
                                                     .flatMap(List::stream)
                                                     .collect(Collectors.toList());

        List<Post> otherPosts = ListUtils.union(postListA, postListB);

        List<Post> myPosts = getPosts();

        return ListUtils.union(otherPosts, myPosts);
    }

    public List<NotificationDTO> getNotifications() {
        List<NotificationDTO> commentNotifications = posts.stream()
                                                          .map(Post::getComments)
                                                          .flatMap(List::stream)
                                                          .filter(l -> !l.getUser().id.equals(id))
                                                          .map(NotificationDTO::new)
                                                          .collect(Collectors.toList());

        List<NotificationDTO> likeNotifications = posts.stream()
                                                       .map(Post::getLikes)
                                                       .flatMap(List::stream)
                                                       .filter(l -> !l.getUser().id.equals(id))
                                                       .map(NotificationDTO::new)
                                                       .collect(Collectors.toList());

        return ListUtils.union(commentNotifications, likeNotifications);
    }
}
