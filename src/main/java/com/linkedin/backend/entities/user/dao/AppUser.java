package com.linkedin.backend.entities.user.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linkedin.backend.entities.connection.Connection;
import com.linkedin.backend.entities.conversation.Conversation;
import com.linkedin.backend.dto.*;
import com.linkedin.backend.entities.message.Message;
import com.linkedin.backend.entities.post.Comment;
import com.linkedin.backend.entities.post.Like;
import com.linkedin.backend.entities.post.Post;
import org.apache.commons.collections4.ListUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
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

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_has_skill",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
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

    @OneToMany(
            mappedBy = "sender",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Message> sentMessages;

    @OneToMany(
            mappedBy = "smallerUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Conversation> smallerConversations;

    @OneToMany(
            mappedBy = "largerUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Conversation> largerConversations;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Job> myCreatedJobs;

    @ManyToMany(mappedBy = "applicants")
    private List<Job> myAppliedJobs;

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

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Conversation> getSmallerConversations() {
        return smallerConversations;
    }

    public void setSmallerConversations(List<Conversation> smallerConversations) {
        this.smallerConversations = smallerConversations;
    }

    public List<Conversation> getLargerConversations() {
        return largerConversations;
    }

    public void setLargerConversations(List<Conversation> largerConversations) {
        this.largerConversations = largerConversations;
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
        return getLikes().stream().map(Like::getPost).collect(Collectors.toList());
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

    public List<Job> getMyCreatedJobs() {
        return myCreatedJobs;
    }

    public void setMyCreatedJobs(List<Job> myCreatedJobs) {
        this.myCreatedJobs = myCreatedJobs;
    }

    public List<Job> getMyAppliedJobs() {
        return myAppliedJobs;
    }

    public void setMyAppliedJobs(List<Job> myAppliedJobs) {
        this.myAppliedJobs = myAppliedJobs;
    }

    public void addConnection(AppUser user) {
        Connection connection = new Connection(this, user);
        requestedConnections.add(connection);
    }

    public List<Integer> getFriendIDs() {
        List<Integer> activeReceived = getReceivedConnections().stream().filter(c -> c.getAccepted() == 1)
                                                                   .map(c -> c.getRequester().getId())
                                                                   .collect(Collectors.toList());

        List<Integer> activeRequested = getRequestedConnections().stream().filter(c -> c.getAccepted() == 1)
                                                                     .map(c -> c.getReceiver().getId())
                                                                     .collect(Collectors.toList());
        return ListUtils.union(activeReceived, activeRequested);
    }

    public List<AppUser> getFriends() {
        List<AppUser> activeReceived = getReceivedConnections().stream()
                .filter(c -> c.getAccepted() == 1)
                .map(Connection::getRequester)
                .collect(Collectors.toList());

        List<AppUser> activeRequested = getRequestedConnections()
                .stream().filter(c -> c.getAccepted() == 1)
                .map(Connection::getReceiver)
                .collect(Collectors.toList());

        return ListUtils.union(activeReceived, activeRequested);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        AppUser that = (AppUser) obj;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public List<Post> getRelevantPosts() {
        Set<Post> set = new LinkedHashSet<>();

        set.addAll(getLikes().stream().map(Like::getPost).collect(Collectors.toList()));
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

        Set<Post> returnSet = new LinkedHashSet<>();
        returnSet.addAll(otherPosts);
        returnSet.addAll(myPosts);

        return new ArrayList<>(returnSet);
    }

    public List<NotificationDTO> getNotifications() {
        List<NotificationDTO> commentNotifications = posts.stream()
                                                          .map(Post::getComments)
                                                          .flatMap(List::stream)
                                                          .filter(l -> !l.getUser().getId().equals(id))
                                                          .map(NotificationDTO::new)
                                                          .collect(Collectors.toList());

        List<NotificationDTO> likeNotifications = posts.stream()
                                                       .map(Post::getLikes)
                                                       .flatMap(List::stream)
                                                       .filter(l -> !l.getUser().getId().equals(id))
                                                       .map(NotificationDTO::new)
                                                       .collect(Collectors.toList());

        return ListUtils.union(commentNotifications, likeNotifications);
    }

    public List<ActiveConversationDTO> getActiveConversations() {
        List<ActiveConversationDTO> listA = getSmallerConversations().stream()
                                                                     .map(c -> new ActiveConversationDTO(c, true))
                                                                     .collect(Collectors.toList());

        List<ActiveConversationDTO> listB = getLargerConversations().stream()
                                                                    .map(c -> new ActiveConversationDTO(c, false))
                                                                    .collect(Collectors.toList());

        return ListUtils.union(listA, listB);
    }
}
