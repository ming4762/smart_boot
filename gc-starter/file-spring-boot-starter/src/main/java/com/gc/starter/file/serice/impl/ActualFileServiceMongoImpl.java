package com.gc.starter.file.serice.impl;

import com.gc.starter.file.serice.ActualFileService;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.*;
import java.util.List;

/**
 * mongo文件执行器
 * @author shizhongming
 * 2020/1/15 8:54 下午
 */
public class ActualFileServiceMongoImpl implements ActualFileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDbFactory dbFactory;

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @return 文件id
     */
    @Override
    public @NotNull String save(@NotNull File file, String filename) throws IOException {
        return this.gridFsTemplate.store(new FileInputStream(file), filename).toString();
    }

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @return 文件ID
     */
    @Override
    public @NotNull String save(@NotNull InputStream inputStream, String filename) throws IOException {
        return this.gridFsTemplate.store(inputStream, filename).toString();
    }

    /**
     * 删除文件
     * @param id 文件ID
     * @return 是否删除成功
     */
    @Override
    public @NotNull Boolean delete(@NotNull String id) {
        this.gridFsTemplate.delete(
                Query.query(Criteria.where("_id").is(id))
        );
        return true;
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     * @return 是否删除成功
     */
    @Override
    public @NotNull Boolean batchDelete(@NotNull List<String> fileIdList) {
        this.gridFsTemplate.delete(
                Query.query(Criteria.where("_id").in(fileIdList))
        );
        return true;
    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NotNull String id) throws FileNotFoundException {
        return GridFSBuckets.create(dbFactory.getDb()).openDownloadStream(new ObjectId(id));
    }

    @Override
    public void download(@NotNull String id, @NotNull OutputStream outputStream) throws IOException {

    }
}
