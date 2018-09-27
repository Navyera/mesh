package com.linkedin.backend.user.controllers;

import com.linkedin.backend.connection.Connection;
import com.linkedin.backend.connection.NotFriendsException;
import com.linkedin.backend.user.handlers.ConnectionNotFoundException;
import com.linkedin.backend.connection.ConnectionService;
import com.linkedin.backend.user.handlers.DuplicateConnectionException;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/friends")
public class FriendController {
    final private AppUserService appUserService;
    final private ConnectionService connectionService;

    @Autowired
    public FriendController(AppUserService appUserService, ConnectionService connectionService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
    }

    @PostMapping("/add/{id}")
    public JSONStatus addFriend(@Valid @RequestHeader(value="Authorization") String auth,
                                @Valid @PathVariable Integer id)
                            throws UserNotFoundException, DuplicateConnectionException {

        JWTUtils token = new JWTUtils(auth);
        Integer requesterID = token.getUserID();
        Integer receiverID = id;

        // Can't friend yourself
        if (requesterID.equals(receiverID)) {
            return new JSONStatus("SELF_ERROR");
        }
        // Check if symmetric relationship exists
        try {
            Connection connection = this.connectionService.findConnectionById(receiverID, requesterID);

            if(connection.getAccepted() == 1){
                return new JSONStatus("ALREADY_ACTIVE");
            }
            connectionService.activateConnection(connection);
            return new JSONStatus("ACTIVATED");

        }
        catch(ConnectionNotFoundException e) {
            AppUser requester = appUserService.findUserById(requesterID);
            AppUser receiver = appUserService.findUserById(receiverID);
            connectionService.addConnection(requester, receiver);
            return new JSONStatus("INACTIVE");

        }


    }

    @PostMapping("/accept/{id}")
    public JSONStatus acceptFriend(@Valid @RequestHeader(value="Authorization") String auth,
                                   @Valid @PathVariable Integer id) throws ConnectionNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        Integer receiverID = token.getUserID();
        Integer requesterID = id;

        connectionService.activateConnection(connectionService.findConnectionById(requesterID, receiverID));

        return new JSONStatus("Successful friend addition");
    }


    @PostMapping("/decline/{id}")
    public JSONStatus declineFriend(@Valid @RequestHeader(value="Authorization") String auth,
                                   @Valid @PathVariable Integer id) throws ConnectionNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        Integer receiverID = token.getUserID();
        Integer requesterID = id;

        connectionService.removeConnection(connectionService.findConnectionById(requesterID, receiverID));

        return new JSONStatus("Successful friend decline");
    }

    @GetMapping("/pending")
    public List<Integer> getPendingRequests(@Valid @RequestHeader(value="Authorization") String auth)
                                                throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return user.getReceivedConnections().stream()
                                            .filter(c -> c.getAccepted() == 0)
                                            .map(c -> c.getRequester().getId())
                                            .collect(Collectors.toList());

    }

    @GetMapping("")
    public List<Integer> getFriends(@Valid @RequestHeader(value="Authorization") String auth)
                                                            throws UserNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return user.getFriendIDs();
    }

    @GetMapping("/{id}")
    public List<Integer> getUserFriends(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id)
                                                                                                  throws UserNotFoundException, NotFriendsException {
        JWTUtils token = new JWTUtils(auth);
        Integer myId = token.getUserID();

        if (!connectionService.friends(id, myId))
            throw new NotFriendsException(id, myId);

        AppUser user = appUserService.findUserById(id);

        return user.getFriendIDs();
    }

}
