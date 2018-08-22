package com.test.prob.model;

import com.test.prob.model.entity.Tag;
import com.test.prob.model.entity.ToDo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
public class ListDaoImpl implements ListDao{


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ListDao.class);

    @Autowired
    SqlSession sqlSession;



    String namespaceForToDo="com.test.prob.model.ListDao.";
    String namespaceForTag="com.test.prob.model.ListDao.";

    @Override
    public List<ToDo> selectAll() throws Exception {
        return sqlSession.selectList(namespaceForToDo+"selectAll");
    }


    @Override
	public List<ToDo> selectAllWithTag(String tag) throws Exception{
//		List<Integer> idxs = sqlSession.selectList(namespaceForTag+"selectIdxsForOneToDo", tag);
//
//		//	System.out.println(tag+" 태그로 찾은 리스트사이즈 "+idxs.size());
//		//  "." 은 검색안돼ㅠㅠ...
//
//		List<ToDo> result = new ArrayList<>();
//		for(int toDoIdx : idxs){
//			ToDo toDoBean = sqlSession.selectOne(namespaceForToDo+"selectOne", toDoIdx);
//			result.add(toDoBean);
//		}

//        select * from todolist where todoidx in (select todoidx from taglist where tag="${tag}");

        List<ToDo> result = sqlSession.selectList(namespaceForToDo+"selectToDoWithTag", tag);
            log.info(result.toString()+"태그검색");
		return result;

	}



    @Override
    public List<Object> selectOne(int idx) throws Exception {

    	ToDo toDoBean = sqlSession.selectOne(namespaceForToDo+"selectOne", idx);

    	int idxForTags = toDoBean.getToDoIdx();
    	List<Tag> tagBean = sqlSession.selectList(namespaceForTag+"selectTagsForOneToDo", idxForTags);
		log.info(idx+"의 태그들은 : "+tagBean);


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
    public void insertOne(ToDo toDoBean) throws Exception {

		dateNullCheck(toDoBean);
    	sqlSession.insert(namespaceForToDo+"insertToDo", toDoBean);
    	int toDoIdx = toDoBean.getToDoIdx();


		insertTags(toDoBean, toDoIdx);
    	//여기에 db 접속 1, todo테이블에 글 하나 넣고
    	//last insert id 받아서 
    	//그 숫자를 내 vo에 셋하고 
    	//db 접속 2, 그 숫자로 tag 테이블에 태그 넣기 
    }

    @Override
    public void editOne(ToDo toDoBean) throws Exception {
    	// db접속 1, todo테이블 업데이트
		// idx를 이용해서
    	// db 접속 2, tag 테이블 업데이트

		dateNullCheck(toDoBean);
    	sqlSession.update(namespaceForTag+"updateWhere", toDoBean);
        editTags(toDoBean);

    	
    }


    public void editTags(ToDo toDoBean){ //태그 수정 메서드

        int idx = toDoBean.getToDoIdx();
        ToDo oldBean = sqlSession.selectOne(namespaceForToDo+"selectOne", idx);
        List<Tag> oldTags = sqlSession.selectList(namespaceForTag+"selectTagsForOneToDo", idx);
        String oldTagStr="";

        for(Tag oldTag : oldTags){
            oldTagStr+=(oldTag.getTag()+" ");
        }
        oldBean.setTags(oldTagStr.trim());

        log.info("기존 데이터 "+oldBean);
        log.info("수정 데이터 "+toDoBean);


        if(toDoBean.getTags().trim().equals("")) {
            log.info("새 태그가 없어서 기존 태그 전부 삭제");
            sqlSession.delete(namespaceForTag+"deleteTag", idx);
            return;
        }

        if(oldBean.getTags()=="") {
            log.info("기존 태그가 없어서 새 태그만 추가");

            insertTags(toDoBean, idx);
            return;
        }

        if(!oldBean.getTags().trim().equals(toDoBean.getTags().trim())) { //태그는 수정됐을 때에만 일괄 삭제 후 다시 넣음;
            sqlSession.delete(namespaceForTag+"deleteTag", idx);

            insertTags(toDoBean, idx);


        }

    }

    //태그 입력 메서드
	public void insertTags(ToDo toDoBean, int idx){

		List<String> tags = Arrays.asList(toDoBean.getTags().trim().split(" "));

		for(String tag : tags) {
			if(!tag.equals("")) {
                log.info("태그 몇번 들어간거;");
				Tag tagBean = new Tag();
				tagBean.setToDoIdx(idx);
				tagBean.setTag(tag);
                log.info("태그 입력 " + tagBean.getTag());
				sqlSession.insert(namespaceForTag + "insertTag", tagBean);
			}
		}

	}

	public void dateNullCheck(ToDo toDoBean){ //날짜 null로 들어온 거 있으면 오늘 날짜로 바꾸는 메서드..
    	//근데 이렇게 할거면...내가 옵셔널을 잘 못 쓰고 있는게 아닐까...?
        //그렇다고......ToDo 객체 내에서 set에 들어올때 설정을 하려고 하면..... 롬복 왜 쓰지...?
		Optional<LocalDate> opt = Optional.ofNullable(toDoBean.getDateFrom());
		if(!opt.isPresent()){
			toDoBean.setDateFrom(LocalDate.now());
		}
		opt= Optional.ofNullable(toDoBean.getDateTo());
		if(!opt.isPresent()){
			toDoBean.setDateTo(LocalDate.now());
		}

	}





}
