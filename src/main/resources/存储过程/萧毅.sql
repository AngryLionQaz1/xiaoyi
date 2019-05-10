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
	user0_.id = #{userId} 
	) AS xs 
WHERE
	xs.id1_3_0_ = #{userId}
	AND xs.uri10_0_4_ =#{uri}
		
		
		
		
		
		
		
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
	user0_.id = #{userId}
	) AS xs 
WHERE
	xs.id1_3_0_ =#{userId} 
	AND xs.code2_1_2_ =#{code}
		
	
		
SELECT
	ax.p_id AS id,
	ax.p_name AS NAME,
	ax.p_code AS CODE,
	ax.p_p_code AS p_code,
	ax.p_p_name AS p_name,
	ax.p_uri AS uri,
	ax.p_details AS details,
	ax.p_flag AS flag,
	ax.p_if_menu AS if_menu,
	ax.p_m_order AS m_order 
FROM
	(
SELECT
	user0_.id AS id1_3_0_,
	permission4_.id AS p_id,
	permission4_.NAME AS p_name,
	permission4_.CODE AS p_code,
	permission4_.p_code AS p_p_code,
	permission4_.p_name AS p_p_name,
	permission4_.uri AS p_uri,
	permission4_.details AS p_details,
	permission4_.flag AS p_flag,
	permission4_.if_menu AS p_if_menu,
	permission4_.m_order AS p_m_order 
FROM
	s_user user0_
	LEFT OUTER JOIN s_user_roles roles1_ ON user0_.id = roles1_.user_id
	LEFT OUTER JOIN s_role role2_ ON roles1_.role_id = role2_.id
	LEFT OUTER JOIN s_role_permissions permission3_ ON role2_.id = permission3_.role_id
	LEFT OUTER JOIN s_permissions permission4_ ON permission3_.permissions_id = permission4_.id 
WHERE
	user0_.id = #{userId}
	) AS ax
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		