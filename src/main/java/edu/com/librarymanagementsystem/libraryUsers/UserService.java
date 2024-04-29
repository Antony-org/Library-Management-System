package edu.com.librarymanagementsystem.libraryUsers;

import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LibraryUser findById(Integer userId){
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<LibraryUser> findAll(){
        return this.userRepository.findAll();
    }

    public LibraryUser save(LibraryUser user){
        // encode plain text password before saving in the database
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public LibraryUser update(Integer userId, LibraryUser user){
        LibraryUser foundUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        foundUser.setUsername(user.getUsername());
        foundUser.setEnabled(user.isEnabled());
        foundUser.setRoles(user.getRoles());

        return this.userRepository.save(foundUser);
    }

    public void delete(Integer userId){
        LibraryUser foundUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username) // Find user from the database
                .map(libraryUser -> new MyUserPrincipal(libraryUser)) // Convert it to UserDetails through MyUserPrinciple
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " is not found"));
    }
}