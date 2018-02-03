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

insert into authorities values('user', 'role_user');
insert into authorities values('admin', 'role_admin');
insert into authorities values('app', 'role_app');

