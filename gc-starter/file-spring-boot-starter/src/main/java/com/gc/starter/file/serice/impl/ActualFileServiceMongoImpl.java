package com.gc.starter.file.serice.impl;

import com.gc.starter.file.serice.ActualFileService;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.List;

    /**
     * mongo文件执行器
     * @author shizhongming
     * 2020/1/15 8:54 下午
     */
    public class ActualFileServiceMongoImpl implements ActualFileService {

        private final GridFsTemplate gridFsTemplate;

        private final MongoDbFactory dbFactory;

        public ActualFileServiceMongoImpl(GridFsTemplate gridFsTemplate, MongoDbFactory dbFactory) {
            this.gridFsTemplate = gridFsTemplate;
            this.dbFactory = dbFactory;
        }


        /**
         * 保存文件
         * @param file 文件
         * @param filename 文件名
         * @return 文件id
         */
        @Override
        public @NonNull String save(@NonNull File file, String filename) throws IOException {
        return this.gridFsTemplate.store(new FileInputStream(file), filename).toString();
    }

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @return 文件ID
     */
    @Override
    public @NonNull
    String save(@NonNull InputStream inputStream, String filename) throws IOException {
        return this.gridFsTemplate.store(inputStream, filename).toString();
    }

    /**
     * 删除文件
     * @param id 文件ID
     */
    @Override
    public void delete(@NonNull String id) {
        this.gridFsTemplate.delete(
                Query.query(Criteria.where("_id").is(id))
        );
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NonNull List<String> fileIdList) {
        this.gridFsTemplate.delete(
                Query.query(Criteria.where("_id").in(fileIdList))
        );
    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) throws FileNotFoundException {
        return GridFSBuckets.create(dbFactory.getDb()).openDownloadStream(new ObjectId(id));
    }

    /**
     * 下载文件并写入输出流
     * 请注意关闭输出流
     * @param id 文件ID
     * @param outputStream 输出流
     * @throws IOException IO异常
     */
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) throws IOException {
        try (InputStream inputStream = this.download(id)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
