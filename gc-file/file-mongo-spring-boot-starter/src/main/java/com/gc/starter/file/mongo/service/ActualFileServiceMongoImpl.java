package com.gc.starter.file.mongo.service;

import com.gc.common.base.exception.IORuntimeException;
import com.gc.common.base.exception.OperationNotSupportedException;
import com.gc.file.common.common.ActualFileServiceRegisterName;
import com.gc.file.common.constants.ActualFileServiceConstants;
import com.gc.file.common.service.ActualFileService;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.List;

/**
 * @author shizhongming
 * 2020/11/14 10:42 下午
 */
public class ActualFileServiceMongoImpl implements ActualFileService {

    private final GridFsTemplate gridFsTemplate;

    private final MongoDatabaseFactory dbFactory;

    public ActualFileServiceMongoImpl(GridFsTemplate gridFsTemplate, MongoDatabaseFactory dbFactory) {
        this.gridFsTemplate = gridFsTemplate;
        this.dbFactory = dbFactory;
    }


    /**
     * 保存文件
     *
     * @param file     文件
     * @param filename 文件名
     * @return 文件id
     */
    @Override
    public @NonNull
    String save(@NonNull File file, String filename) {
        try {
            return this.gridFsTemplate.store(new FileInputStream(file), filename).toString();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 保存文件
     *
     * @param inputStream 文件流
     * @param filename    文件名
     * @return 文件ID
     */
    @Override
    public @NonNull
    String save(@NonNull InputStream inputStream, String filename) {
        return this.gridFsTemplate.store(inputStream, filename).toString();
    }

    /**
     * 删除文件
     *
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
     *
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
     *
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) {
        return GridFSBuckets.create(dbFactory.getMongoDatabase()).openDownloadStream(new ObjectId(id));
    }

    /**
     * 下载文件并写入输出流
     * 请注意关闭输出流
     *
     * @param id           文件ID
     * @param outputStream 输出流
     */
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {
        try (InputStream inputStream = this.download(id)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取文件的绝对路径
     *
     * @param id 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull String id) {
        // TODO: i18n
        throw new OperationNotSupportedException("mongoDB不支持获取文件绝对路径");
    }

    /**
     * 获取注册名字
     * @return 注册名字
     */
    @Override
    public ActualFileServiceRegisterName getRegisterName() {
        return ActualFileServiceRegisterName.builder()
                .beanName(ActualFileServiceConstants.MONGO.getServiceName())
                .dbName(ActualFileServiceConstants.MONGO.name())
                .build();
    }
}
