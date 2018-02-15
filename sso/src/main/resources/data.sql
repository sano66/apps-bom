/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  sano
 * Created: Feb 3, 2018
 */

insert into users values('user','password',true);
insert into users values('admin','password',true);
insert into users values('app','password',true);
insert into users values('system','password',true);
insert into users values('doma','password',true);
insert into users values('ACID','password',true);

insert into authorities values('user', 'role_user');
insert into authorities values('admin', 'role_admin');
insert into authorities values('app', 'role_app');
insert into authorities values('doma', 'role_app');
insert into authorities values('ACID', 'role_app');

INSERT INTO my_branches VALUES('ny','New York', 'USA');
INSERT INTO my_branches VALUES('paris','Paris', 'France');
INSERT INTO my_branches VALUES('london','London', 'UK');
INSERT INTO my_branches VALUES('tokyo','tokyo', 'Japan');

INSERT INTO my_users VALUES('doma','yourname@yourdomain.com','ABC', 'XYX');
INSERT INTO my_users VALUES('ACID','acid@yourdomain.com','ABC', 'XYX');

INSERT INTO my_signon VALUES('ny', 'doma');
INSERT INTO my_signon VALUES('paris', 'doma');
INSERT INTO my_signon VALUES('tokyo', 'ACID');
INSERT INTO my_signon VALUES('paris', 'ACID');
INSERT INTO my_signon VALUES('london', 'ACID');

