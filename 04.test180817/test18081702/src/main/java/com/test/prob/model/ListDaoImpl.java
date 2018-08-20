package com.test.prob.model;

import com.test.prob.model.entity.TagVo;
import com.test.prob.model.entity.ToDoVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ListDaoImpl implements ListDao{

    @Autowired
    SqlSession sqlSession;

    String namespaceForToDo="com.test.prob.model.ListDao.";
    String namespaceForTag="com.test.prob.model.ListDao.";

    @Override
    public List<ToDoVo> selectAll() throws Exception {

        return sqlSession.selectList(namespaceForToDo+"selectAll");
    }


    @Override
	public List<ToDoVo> selectAllWithTag(String tag) throws Exception{
		List<Integer> idxs = sqlSession.selectList(namespaceForTag+"selectIdxsForOneToDo", tag);

		//	System.out.println(tag+" 태그로 찾은 리스트사이즈 "+idxs.size());
		//  "." 은 검색안돼ㅠㅠ...

		List<ToDoVo> result = new ArrayList<>();
		for(int toDoIdx : idxs){
			ToDoVo toDoBean = sqlSession.selectOne(namespaceForToDo+"selectOne", toDoIdx);
			result.add(toDoBean);
		}

		return result;

	}



    @Override
    public List<Object> selectOne(int idx) throws Exception {

    	ToDoVo toDoBean = sqlSession.selectOne(namespaceForToDo+"selectOne", idx);

    	int idxForTags = toDoBean.getToDoIdx();
    	List<TagVo> tagBean = sqlSession.selectList(namespaceForTag+"selectTagsForOneToDo", idxForTags);
		System.out.println(idx+"의 태그들은 : "+tagBean);


		List<Object> list = new ArrayList<>();
		list.add(toDoBean); //객체 하나
		list.add(tagBean); //list 객체

    	return list;
    	//db 접속 1, todo에서 idx 가져오고 
    	//db 접속 2 tag에서 idx로 태그 가져오기 
    	
    }

    @Override
    public void delOne(int idx) throws Exception {
    	//db 접속 1, todo에서 idx로 지우고 
    	//db 접속 2, tag 에서 idx로 지우기

    	sqlSession.delete(namespaceForTag+"deleteOne", idx);
    	sqlSession.delete(namespaceForTag+"deleteTag", idx);
    	
    }

    @Override
    public void insertOne(ToDoVo toDoBean) throws Exception {

    	sqlSession.insert(namespaceForToDo+"insertToDo", toDoBean);
    	int toDoIdx = toDoBean.getToDoIdx();


		insertTags(toDoBean, toDoIdx);
    	//여기에 db 접속 1, todo테이블에 글 하나 넣고
    	//last insert id 받아서 
    	//그 숫자를 내 vo에 셋하고 
    	//db 접속 2, 그 숫자로 tag 테이블에 태그 넣기 
    }

    @Override
    public void editOne(ToDoVo toDoBean) throws Exception {
    	// db접속 1, todo테이블 업데이트
		// idx를 이용해서
    	// db 접속 2, tag 테이블 업데이트 

    	sqlSession.update(namespaceForTag+"updateWhere", toDoBean);
    	
    	int idx = toDoBean.getToDoIdx();
    	ToDoVo oldBean = sqlSession.selectOne(namespaceForToDo+"selectOne", idx);
    	List<TagVo> oldTags = sqlSession.selectList(namespaceForTag+"selectTagsForOneToDo", idx);
		String oldTagStr="";

    	for(TagVo oldTag : oldTags){
    		oldTagStr+=(oldTag.getTag()+" ");
		}
		oldBean.setTags(oldTagStr.trim());

    	System.out.println("기존 데이터 "+oldBean);
    	System.out.println("수정 데이터 "+toDoBean);
    	
    	
    	if(toDoBean.getTags().trim().equals("")) {
    		System.out.println("새 태그가 없어서 기존 태그 전부 삭제");
    		sqlSession.delete(namespaceForTag+"deleteTag", idx);
    		return;
    	}

    	if(oldBean.getTags()=="") {
    		System.out.println("기존 태그가 없어서 새 태그만 추가");

			insertTags(toDoBean, idx);
    		return;
    	}

    	if(!oldBean.getTags().trim().equals(toDoBean.getTags().trim())) { //태그는 수정됐을 때에만 일괄 삭제 후 다시 넣음;
    		sqlSession.delete(namespaceForTag+"deleteTag", idx);

			insertTags(toDoBean, idx);


    	}
    	

    	
    }

    //태그 입력 메서드
	public void insertTags(ToDoVo toDoBean, int idx){

		List<String> tags = Arrays.asList(toDoBean.getTags().trim().split(" "));

		for(String tag : tags) {
			if(!tag.equals("")) {
				System.out.println("태그 몇번 들어간거;");
				TagVo tagBean = new TagVo();
				tagBean.setToDoIdx(idx);
				tagBean.setTag(tag);
				System.out.println("태그 입력 " + tagBean.getTag());
				sqlSession.insert(namespaceForTag + "insertTag", tagBean);
			}
		}



	}

}
