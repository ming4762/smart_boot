package com.smart.starter.file.service.impl

import com.mongodb.client.gridfs.GridFSBuckets
import com.smart.starter.file.service.ActualFileService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import java.io.File
import java.io.InputStream

/**
 * 文件服务mongo实现
 * @author ming
 * 2019/6/15 下午8:06
 */
class ActualFileServiceMongoImpl : ActualFileService {

    @Autowired
    private lateinit var gridFsTemplate: GridFsTemplate

    /**
     * MongoDB工厂
     */
    @Autowired
    private lateinit var dbFactory: MongoDbFactory

    /**
     * 保存文件
     */
    override fun save(file: File, filename: String?): String {
        return this.gridFsTemplate.store(file.inputStream(), filename).toString()
    }

    /**
     * 保存文件
     */
    override fun save(inputStream: InputStream, filename: String?): String {
        return this.gridFsTemplate.store(inputStream, filename).toString()
    }

    /**
     * 删除文件
     */
    override fun delete(id: String): Boolean {
        this.gridFsTemplate.delete(
                Query.query(Criteria.where("_id").`is`(id))
        )
        return true
    }

    override fun download(id: String): InputStream {
        return GridFSBuckets.create(dbFactory.db).openDownloadStream(ObjectId(id))
    }
}