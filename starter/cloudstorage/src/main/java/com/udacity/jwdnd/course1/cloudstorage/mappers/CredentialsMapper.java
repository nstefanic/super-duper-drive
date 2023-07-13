package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.SavedCredential;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialsMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    SavedCredential getCredential(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    SavedCredential[] getCredentials(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, pwdkey, password, userid) " +
            "VALUES(#{url}, #{username}, #{pwdKey}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(SavedCredential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, pwdkey = #{pwdKey}, password = #{password} WHERE credentialId = #{credentialId}")
    int update(SavedCredential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}" + "AND userid = #{userId}")
    void delete(Integer credentialId, Integer userId);

}
