
CREATE DEFINER=`root`@`%` PROCEDURE `Uri`(IN pUri VARCHAR(255))
BEGIN
     select id from s_permissions where uri=pUri;
END;


CREATE DEFINER=`root`@`%` PROCEDURE `Permissions`(IN pUserId VARCHAR(255),IN pUri VARCHAR(255))
BEGIN
     SELECT
	xs.id1_3_0_ AS userId,
	xs.uri10_0_4_ AS uri 
FROM
	(
SELECT
	user0_.id AS id1_3_0_,
	roles1_.user_id AS user_id1_4_1_,
	role2_.id AS role_id2_4_1_,
	role2_.id AS id1_1_2_,
	role2_.CODE AS code2_1_2_,
	role2_.NAME AS name3_1_2_,
	permission3_.role_id AS role_id1_2_3_,
	permission4_.id AS permissi2_2_3_,
	permission4_.id AS id1_0_4_,
	permission4_.uri AS uri10_0_4_ 
FROM
	s_user user0_
	LEFT OUTER JOIN s_user_roles roles1_ ON user0_.id = roles1_.user_id
	LEFT OUTER JOIN s_role role2_ ON roles1_.role_id = role2_.id
	LEFT OUTER JOIN s_role_permissions permission3_ ON role2_.id = permission3_.role_id
	LEFT OUTER JOIN s_permissions permission4_ ON permission3_.permissions_id = permission4_.id 
WHERE
	user0_.id = pUserId
	) AS xs 
WHERE
	xs.id1_3_0_ = pUserId
	AND xs.uri10_0_4_ = pUri;
END;



CREATE DEFINER=`root`@`%` PROCEDURE `Role`(IN pUserId VARCHAR(255),IN pCode VARCHAR(255))
BEGIN
     SELECT
	xs.id1_3_0_ AS id,
	xs.code2_1_2_ AS CODE 
FROM
	(
SELECT
	user0_.id AS id1_3_0_,
	user0_.create_time AS create_t2_3_0_,
	user0_.PASSWORD AS password3_3_0_,
	user0_.username AS username4_3_0_,
	roles1_.user_id AS user_id1_4_1_,
	role2_.id AS role_id2_4_1_,
	role2_.id AS id1_1_2_,
	role2_.CODE AS code2_1_2_,
	role2_.NAME AS name3_1_2_ 
FROM
	s_user user0_
	LEFT OUTER JOIN s_user_roles roles1_ ON user0_.id = roles1_.user_id
	LEFT OUTER JOIN s_role role2_ ON roles1_.role_id = role2_.id 
WHERE
	user0_.id = pUserId
	) AS xs 
WHERE
	xs.id1_3_0_ = pUserId
	AND xs.code2_1_2_ = pCode;
END;