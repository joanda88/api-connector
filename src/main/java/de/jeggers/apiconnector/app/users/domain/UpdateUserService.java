package de.jeggers.apiconnector.app.users.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateUserService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateUserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FetchUserService fetchUserService;

    @Scheduled(fixedRate = 10000)
    public void updateUsersFromAPI()
    {
        LOG.debug("start new synchronisation to update users...");
        final Optional<List<User>> users = fetchUserService.getUsers();
        users.ifPresent(userList -> userRepository.synchronizeUsersInDB(userList, false));
    }
}
