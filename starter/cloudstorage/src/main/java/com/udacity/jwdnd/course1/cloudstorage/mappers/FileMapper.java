package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.UserFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    UserFile getFileByName(String fileName);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}" +
            "AND userid = #{userId}")
    UserFile getFileByIdAndUserId(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    UserFile[] getFilesByUserId(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(UserFile file);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    UserFile getFileByNameAndUserId(String fileName, Integer userId);
}
