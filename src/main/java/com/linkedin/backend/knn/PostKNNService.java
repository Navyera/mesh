package com.linkedin.backend.knn;

import com.linkedin.backend.entities.comment.Comment;
import com.linkedin.backend.entities.connection.ConnectionService;
import com.linkedin.backend.entities.post.Post;
import com.linkedin.backend.entities.post.PostRepository;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.user.AppUserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostKNNService {
    private final PostRepository postRepository;
    private final AppUserService appUserService;
    private final ConnectionService friendService;

    public class KNNResult {
        private AppUser user;
        private Double distance;

        public KNNResult(AppUser user, Double distance) {
            this.user = user;
            this.distance = distance;
        }

        public AppUser getUser() {
            return user;
        }

        public void setUser(AppUser user) {
            this.user = user;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

    }

    public class PostScore {
        private Post post;
        private Double score;

        public PostScore(Post post, Long maxTime, Long minTime, List<KNNResult> knn, double a) {
            this.post = post;

            double normalizer = maxTime - minTime;
            if (normalizer == 0.0)
                normalizer = 1.0;

            this.score = (1.0 - a) * (maxTime - post.getDate().getTime()/1000)/normalizer;

            for (KNNResult knnResult : knn) {
                if (knnResult.getUser().getId().equals(post.getUser().getId())) {
                    this.score -= a * knnResult.getDistance();

                    break;
                }
            }
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }
    }

    public PostKNNService(PostRepository postRepository, AppUserService appUserService, ConnectionService friendService) {
        this.postRepository = postRepository;
        this.appUserService = appUserService;
        this.friendService = friendService;
    }

    private double distance(List<Double> myVector, List<Double> otherVector) {
        double distance = 0.0;

        for (int i = 0; i < myVector.size(); ++i)
            distance += Math.pow(myVector.get(i) - otherVector.get(i), 2);

        return Math.sqrt(distance);
    }

    private List<Double> createVector(AppUser user, Map<Post, Integer> index) {
        List<Double> myVector = new ArrayList<>(Collections.nCopies(index.keySet().size(), 0.0));

        // Hash my posts, so we have fast access to them.
        for (Post post : user.getLikedPosts())
            myVector.set(index.get(post), 1.0);

        for (Post post : user.getComments().stream().map(Comment::getPost).collect(Collectors.toList()))
            myVector.set(index.get(post), 1.0);

        return myVector;
    }

    public List<KNNResult> getUserKNN(AppUser user, Integer k) {
        // Map posts, to make post -> vector index mapping.
        Map<Post, Integer> postIndices = new HashMap<>();

        PriorityQueue<KNNResult> pqueue = new PriorityQueue<>(Comparator.comparing(KNNResult::getDistance));

        int idx = 0;
        for (Post post : postRepository.findAll())
            postIndices.put(post, idx++);

        List<Double> myVector = createVector(user, postIndices);

        double maxDistance = 0.0;

        // Iterate for every user other than me and my friends.
        for (AppUser other : appUserService.findAll()) {
            if (other.equals(user) || friendService.friends(other.getId(), user.getId()))
                continue;

            List<Double> otherVector = createVector(other, postIndices);

            double distance = distance(myVector, otherVector);

            maxDistance = distance > maxDistance ? distance : maxDistance;

            pqueue.add(new KNNResult(other, distance));
        }

        if (maxDistance == 0.0)
            maxDistance = 1.0;

        final double normalizer = maxDistance;

        return pqueue
                .stream()
                .sorted(Comparator.comparing(KNNResult::getDistance))
                .map(e -> new KNNResult(e.user, e.distance/normalizer))
                .limit(k)
                .collect(Collectors.toList());
    }

    public List<Post> generateUserKNN(AppUser user) {
        List<KNNResult> knn = getUserKNN(user, 2);

        List<Post> normalFeed = user.getUserFeed();

        List<Post> outer = knn.stream()
                .map(KNNResult::getUser)
                .map(AppUser::getPosts)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Set<Post> allPosts = new LinkedHashSet<>();

        allPosts.addAll(normalFeed);
        allPosts.addAll(outer);

        Date minDate = allPosts.stream().min(Comparator.comparing(Post::getDate)).map(Post::getDate).orElse(new Date(0L));
        Date maxDate = allPosts.stream().max(Comparator.comparing(Post::getDate)).map(Post::getDate).orElse(new Date());

        List<Post> userFeed = allPosts
                .stream().map(p -> new PostScore(p, maxDate.getTime()/1000, minDate.getTime()/1000, knn, 0.001))
                .sorted(Comparator.comparing(PostScore::getScore))
                .map(PostScore::getPost)
                .collect(Collectors.toList());

        return userFeed;
}
}
