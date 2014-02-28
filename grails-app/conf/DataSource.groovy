dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

dataSource_importacion{
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    driverClassName = 'com.mysql.jdbc.Driver'
    username = 'root'
    password = 'sys'
    url = 'jdbc:mysql://localhost/produccion'
    dbCreate = ''
    readOnly=true
    properties {
        maxActive = 5
        maxIdle = 3
        minIdle = 2
        initialSize = 3
        minEvictableIdleTimeMillis = 60000
        timeBetweenEvictionRunsMillis = 60000
        maxWait = 10000
        validationQuery = "/* ping */"
    }
}

// environment specific settings
environments {
    development {
        dataSource{
            dialect = org.hibernate.dialect.MySQL5InnoDBDialect
            driverClassName = 'com.mysql.jdbc.Driver'
            username = 'root'
            password = 'sys'
            url = 'jdbc:mysql://localhost/cfdi_server'
            dbCreate = 'update'
            properties {
            maxActive = 2
            maxIdle = 1
            minIdle = 1
            initialSize = 1
            minEvictableIdleTimeMillis = 60000
            timeBetweenEvictionRunsMillis = 60000
            maxWait = 10000
            validationQuery = "/* ping */"
    }
}
        dataSource_importacion{
            dbCreate = ""
            url = 'jdbc:mysql://10.10.1.228/produccion'
        }
        
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=false
               validationQuery="SELECT 1"
               jdbcInterceptors="ConnectionState"
            }
        }
    }
}
