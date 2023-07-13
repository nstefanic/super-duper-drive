package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.SavedCredential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CredentialsService {

    private final CredentialsMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public SavedCredential[] getCredentials(Integer userId) {
        return credentialMapper.getCredentials(userId);
    }

    private int createCredential(SavedCredential credential) {
        SavedCredential credentialWithEncryptedPassword = encryptPassword(credential);
        return credentialMapper.insert(credentialWithEncryptedPassword);
    }

    private int updateCredential(SavedCredential credential) {
        SavedCredential credentialWithEncryptedPassword = encryptPassword(credential);
        return credentialMapper.update(credentialWithEncryptedPassword);
    }

    public int addOrUpdateCredential(SavedCredential credential) {
        if (credential.getCredentialId() == null) {
            return createCredential(credential);
        } else {
            return updateCredential(credential);
        }
    }

    public void deleteCredential(Integer credentialId, Integer userId) {
        credentialMapper.delete(credentialId, userId);
    }

    private SavedCredential encryptPassword(SavedCredential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setPwdKey(encodedKey);
        return credential;
    }

}
