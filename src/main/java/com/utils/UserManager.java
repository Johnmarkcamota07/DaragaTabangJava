package com.utils;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {
    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());
    private static final String USER_FILE = "data/users.db";

    public String authenticate(String username, String password) {
        File file = new File(USER_FILE);
        if (!file.exists()) return null;
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Format: ROLE|Username|Password|FullName
                String[] parts = line.split("\\|");

                if (parts.length >= 4 && parts[1].equals(username) && parts[2].equals(password)) {
                    return parts[0] + "|" + parts[3]; 
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "FILE NOT FOUND",e);
        }
        return null;
    }

    public boolean registerUser(String username, String password, String fullName) {
        if (isUsernameTaken(username)) return false;

        try (FileWriter fw = new FileWriter(USER_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            String record = "USER|" + username + "|" + password + "|" + fullName;///forces na iinput sa file na once mag sign up then USER na sila
            bw.write("\n" + record);
            bw.newLine();
            return true;

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "FILE NOT FOUND", e);
            return false;
        }
    }

    private boolean isUsernameTaken(String username) {
        File file = new File(USER_FILE);
        if (!file.exists()) return false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split("\\|");
                if (parts.length > 1 && parts[1].equals(username)) return true;
            }
        } catch (FileNotFoundException e) { }
        return false;
    }
}
