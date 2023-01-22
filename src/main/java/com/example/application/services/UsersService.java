package com.example.application.services;

import com.example.application.entities.User;
import com.example.application.repos.UsersRepo;
import com.goebl.david.Webb;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

import com.password4j.*;

@Service
public class UsersService {

    private final UsersRepo usersRepo;

    public UsersService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public List<User> findAll() {
        return this.usersRepo.findAll();
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return
     */
    public User create(String firstName, String lastName, String email) {

        // create password
        String password = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build().generate(8);

        return this.create(firstName, lastName, email, password);
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password plain password
     * @return
     */
    public User create(String firstName, String lastName, String email, String password) {
        // hash it
        String hashed = Password.hash(password).addRandomSalt(16).withArgon2().getResult();

        return usersRepo.save(
            new User(email, hashed, firstName, lastName)
        );
    }

    public User update(User userEntity) {
        return usersRepo.save(userEntity);
    }

    /**
     *
     * @param receipt
     * @param password
     */
    public static void sendPasswordViaEmail(String receipt, String password) {
        Webb request = Webb.create();
        request.post("https://sensualistic-biases.000webhostapp.com/send-mail.php")
                .param("receipt", receipt)
                .param("subject", "Your cinema password")
                .param("message", "Hi there!\nYour cinema password is: "+password)
                .ensureSuccess()
                .asVoid();
    }

    /**
     *
     * @return plain password
     */
    public static String generatePassword() {
        // create password
        return new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build().generate(8);
    }

    /**
     *
     * @param password plain password
     * @return
     */
    public static String hashPassword(String password) {
        return Password.hash(password).addRandomSalt(16).withArgon2().getResult();
    }

    /**
     *
     * @param user
     */
    public void delete(User user) {
        usersRepo.delete(user);
    }
}
