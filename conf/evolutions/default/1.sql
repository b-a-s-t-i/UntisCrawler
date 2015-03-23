# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "timetableevents" ("id" UUID NOT NULL PRIMARY KEY,"userId" UUID NOT NULL,"configId" UUID NOT NULL,"datetime" TIMESTAMP NOT NULL,"timetableEvent" VARCHAR(200000) NOT NULL);
create table "untisconfig" ("id" UUID NOT NULL PRIMARY KEY,"uid" UUID NOT NULL,"url" VARCHAR(254) NOT NULL,"school" VARCHAR(254) NOT NULL,"elementType" INTEGER NOT NULL,"elementId" INTEGER NOT NULL,"untisuser" VARCHAR(254) NOT NULL,"untispassword" VARCHAR(254) NOT NULL,"error" BOOLEAN NOT NULL);
create table "user" ("email" VARCHAR(254) NOT NULL,"id" UUID NOT NULL PRIMARY KEY,"dateTime" TIMESTAMP NOT NULL);
create unique index "email_index" on "user" ("email");
create table "usernotification" ("id" UUID NOT NULL PRIMARY KEY,"userId" UUID NOT NULL,"type" INTEGER NOT NULL,"address" VARCHAR(254) NOT NULL);
alter table "timetableevents" add constraint "fk_config" foreign key("userId") references "user"("id") on update NO ACTION on delete NO ACTION;
alter table "timetableevents" add constraint "fk_user" foreign key("configId") references "untisconfig"("id") on update NO ACTION on delete NO ACTION;
alter table "untisconfig" add constraint "userIdFk" foreign key("uid") references "user"("id") on update NO ACTION on delete NO ACTION;
alter table "usernotification" add constraint "userId" foreign key("userId") references "user"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "usernotification" drop constraint "userId";
alter table "untisconfig" drop constraint "userIdFk";
alter table "timetableevents" drop constraint "fk_config";
alter table "timetableevents" drop constraint "fk_user";
drop table "usernotification";
drop table "user";
drop table "untisconfig";
drop table "timetableevents";

