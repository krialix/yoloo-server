package com.yoloo.server

import com.google.api.gax.rpc.TransportChannelProvider
import com.yoloo.server.post.util.LocalChannelProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.ldap.LdapDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication(
    exclude = [
        ActiveMQAutoConfiguration::class,
        AopAutoConfiguration::class,
        ArtemisAutoConfiguration::class,
        BatchAutoConfiguration::class,
        CassandraAutoConfiguration::class,
        CassandraDataAutoConfiguration::class,
        CassandraReactiveDataAutoConfiguration::class,
        CassandraReactiveRepositoriesAutoConfiguration::class,
        CassandraRepositoriesAutoConfiguration::class,
        CouchbaseAutoConfiguration::class,
        CouchbaseDataAutoConfiguration::class,
        CouchbaseReactiveDataAutoConfiguration::class,
        CouchbaseReactiveRepositoriesAutoConfiguration::class,
        CouchbaseRepositoriesAutoConfiguration::class,
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        ElasticsearchAutoConfiguration::class,
        ElasticsearchDataAutoConfiguration::class,
        ElasticsearchRepositoriesAutoConfiguration::class,
        EmbeddedLdapAutoConfiguration::class,
        EmbeddedMongoAutoConfiguration::class,
        EmbeddedWebServerFactoryCustomizerAutoConfiguration::class,
        ErrorWebFluxAutoConfiguration::class,
        FlywayAutoConfiguration::class,
        FreeMarkerAutoConfiguration::class,
        GroovyTemplateAutoConfiguration::class,
        H2ConsoleAutoConfiguration::class,
        HazelcastAutoConfiguration::class,
        HazelcastJpaDependencyAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        JpaRepositoriesAutoConfiguration::class,
        KafkaAutoConfiguration::class,
        LdapAutoConfiguration::class,
        LdapDataAutoConfiguration::class,
        LdapRepositoriesAutoConfiguration::class,
        LiquibaseAutoConfiguration::class,
        MongoAutoConfiguration::class,
        MongoDataAutoConfiguration::class,
        MongoReactiveAutoConfiguration::class,
        MongoReactiveDataAutoConfiguration::class,
        MongoReactiveRepositoriesAutoConfiguration::class,
        MongoRepositoriesAutoConfiguration::class,
        MustacheAutoConfiguration::class,
        Neo4jDataAutoConfiguration::class,
        Neo4jRepositoriesAutoConfiguration::class,
        RabbitAutoConfiguration::class,
        RedisAutoConfiguration::class,
        RedisReactiveAutoConfiguration::class,
        RedisRepositoriesAutoConfiguration::class,
        SolrAutoConfiguration::class,
        SolrRepositoriesAutoConfiguration::class
    ]
)
class PostApplication : SpringBootServletInitializer() {

    /*@Profile("dev")
    @Bean*/
    fun transportChannelProvider(): TransportChannelProvider {
        return LocalChannelProvider()
    }
}

fun main(args: Array<String>) {
    runApplication<PostApplication>(*args)
}
