package com.test.prob.model;

import com.test.prob.model.entity.Tag;
import com.test.prob.model.entity.ToDo;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;


@Repository
public class ListDaoImpl implements ListDao{

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ListDaoImpl.class);

    @Autowired
    SqlSession sqlSession;

    String namespaceForToDo="com.test.prob.model.ListDao.";
    String namespaceForTag="com.test.prob.model.ListDao.";

    public ListDaoImpl(){}

   public  ListDaoImpl(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }


    @Override
    public List<ToDo> selectAll()  {
        return sqlSession.selectList(namespaceForToDo+"selectAll");
    }


    @Override
	public List<ToDo> selectAllWithTag(String tag) {
        List<ToDo> result = sqlSession.selectList(namespaceForToDo+"selectToDoWithTag", tag);
        log.info(" 태그 검색 : "+result.toString());
		return result;

	}



    @Override
    public List<Object> selectOne(int idx) {

    	ToDo toDoBean = sqlSession.selectOne(namespaceForToDo+"selectOne", idx);
    	int idxForTags = toDoBean.getToDoIdx();

    	List<Tag> tagBean = sqlSession.selectList(namespaceForTag+"selectTagsForOneToDo", idxForTags);
		log.info(idx+"의 태그 : "+tagBean);

		List<Object> list = new ArrayList<>();
		list.add(toDoBean); //객체 하나
		list.add(tagBean); //list 객체

    	return list;
    	//db 접속 1, todo에서 idx 가져오고 
    	//db 접속 2 tag에서 idx로 태그 가져오기 
    	
    }

    @Override
    public void delOne(int idx) {
    	//db 접속 1, todo에서 idx로 지우고 
    	//db 접속 2, tag 에서 idx로 지우기

    	sqlSession.delete(namespaceForTag+"deleteOne", idx);
    	sqlSession.delete(namespaceForTag+"deleteTag", idx);
    	
    }

    @Override
    public void insertOne(ToDo toDoBean) {


        propNullCheck(toDoBean);
    	sqlSession.insert(namespaceForToDo+"insertToDo", toDoBean);
    	int toDoIdx = toDoBean.getToDoIdx();

		insertTags(toDoBean, toDoIdx);
    	//여기에 db 접속 1, todo테이블에 글 하나 넣고
    	//last insert id 받아서 
    	//그 숫자를 내 vo에 셋하고 
    	//db 접속 2, 그 숫자로 tag 테이블에 태그 넣기 
    }

    @Override
    public void editOne(ToDo toDoBean) throws Exception{ //실패시 화면에 실패했다고 출력 기능 //crud 전부 익셉션 클래스 따로..
    	// db접속 1, todo테이블 업데이트
		// idx를 이용해서
    	// db 접속 2, tag 테이블 업데이트
        log.info("왜 오류요");
        propNullCheck(toDoBean);
    	sqlSession.update(namespaceForTag+"updateWhere", toDoBean);
        editTags(toDoBean);

    }


    public void editTags(ToDo toDoBean){ //태그 수정 메서드 //옵셔널로 변경하기
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

    //태그 입력 메서드 //옵셔널로 변경하기
	public void insertTags(ToDo toDoBean, int idx){

		List<String> tags = Arrays.asList(toDoBean.getTags().trim().split(" "));

		for(String tag : tags) {
			if(!tag.equals("")) {
				Tag tagBean = new Tag();
				tagBean.setToDoIdx(idx);
				tagBean.setTag(tag);
                log.info("태그 입력 " + tagBean.getTag());
				sqlSession.insert(namespaceForTag + "insertTag", tagBean);
			}
		}

	}

	public void propNullCheck(ToDo toDoBean){ //날짜/할일 null로 들어온 거 있으면 바꾸는 메서드..
        log.info("prop null check");
        // properties 널 체크로 변경....
    	//내가 옵셔널을 잘 못 쓰고 있는게 아닐까...?
		Optional<LocalDate> optDate = Optional.ofNullable(toDoBean.getDateFrom());
		if(!optDate.isPresent()){
		    log.info("날짜가 널...");
			toDoBean.setDateFrom(LocalDate.now());
		}
		optDate= Optional.ofNullable(toDoBean.getDateTo());
		if(!optDate.isPresent()){
            log.info("날짜가 널...2");
			toDoBean.setDateTo(LocalDate.now());
		}

        Optional<String> optTitle = Optional.ofNullable(toDoBean.getTitle().trim());
		if(!optTitle.isPresent()){ //여기 어쩌지?
		    toDoBean.setTitle("할 일이 설정되지 않았습니다.");
        }
	}





}
