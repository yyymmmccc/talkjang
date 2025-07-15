package com.talkjang.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -858475216L;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final StringPath id = createString("id");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final EnumPath<com.talkjang.demo.common.enums.UserProvider> provider = createEnum("provider", com.talkjang.demo.common.enums.UserProvider.class);

    public final EnumPath<com.talkjang.demo.common.enums.UserRole> role = createEnum("role", com.talkjang.demo.common.enums.UserRole.class);

    public final StringPath shopName = createString("shopName");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

