package com.jafa.repository;

import com.jafa.domain.LikeDTO;

public interface ArticleLikeRepository {
	void insert(LikeDTO likeDTO);
	
	void delete(LikeDTO likeDTO);
	
	LikeDTO get(LikeDTO likeDTO);	
}
