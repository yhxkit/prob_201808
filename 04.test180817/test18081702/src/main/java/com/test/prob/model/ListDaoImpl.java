package com.test.prob.model;

import com.test.prob.model.entity.Tag;
import com.test.prob.model.entity.ToDo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class ListDaoImpl implements ListDao{

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ListDaoImpl.class);

    @Autowired
    SqlSession sqlSession;

    String namespaceForToDoList="com.test.prob.model.ListDao.";


    public ListDaoImpl(){}

   public  ListDaoImpl(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }


    @Override
    public List<ToDo> selectAll()  {
        return sqlSession.selectList(namespaceForToDoList+"selectAll");
    }


    @Override
	public List<ToDo> selectAllWithTag(String tag) {
        List<ToDo> result = sqlSession.selectList(namespaceForToDoList+"selectToDoWithTag", tag);
        log.info(" 태그 검색 : "+result.toString());
		return result;

	}



    @Override
    public List<Object> selectOne(int idx) {

    	ToDo toDoBean = sqlSession.selectOne(namespaceForToDoList+"selectOne", idx);
    	int idxForTags = toDoBean.getToDoIdx();

    	List<Tag> tagBean = sqlSession.selectList(namespaceForToDoList+"selectTagsForOneToDo", idxForTags);
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

    	int delToDo = sqlSession.delete(namespaceForToDoList+"deleteOne", idx);
        int delTag = sqlSession.delete(namespaceForToDoList+"deleteTag", idx);

        log.info("삭제 결과"+delToDo+":"+delTag);
    	
    }

    @Override
    public void insertOne(ToDo toDoBean) {


    	sqlSession.insert(namespaceForToDoList+"insertToDo", toDoBean);
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
    	int rst = sqlSession.update(namespaceForToDoList+"updateWhere", toDoBean);
        log.info("수정.."+ rst +" "+toDoBean);
        editTags(toDoBean);
    }


    public void editTags(ToDo toDoBean){ //태그 수정 메서드... 여기 어떡하지..?
        int idx = toDoBean.getToDoIdx();

        List<Tag> oldTags = sqlSession.selectList(namespaceForToDoList+"selectTagsForOneToDo", idx);
        String oldTagStr="";
        for(Tag oldTag : oldTags){
            oldTagStr+=(oldTag.getTag()+" ");
        }

        log.info("기존 데이터 "+oldTagStr);
        log.info("수정 데이터 "+toDoBean.getTags());


        if(toDoBean.getTags().trim().equals("")) {
            //새 태그가 비어서 기존 태그 전부 삭제
            sqlSession.delete(namespaceForToDoList+"deleteTag", idx);
            return;
        }

        if(oldTagStr=="") {
            //기존 태그가 없어서 새 태그만 추가
            insertTags(toDoBean, idx);
            return;
        }

        if(!oldTagStr.trim().equals(toDoBean.getTags().trim())) { //태그는 수정됐을 때에만 일괄 삭제 후 다시 넣음;
            sqlSession.delete(namespaceForToDoList+"deleteTag", idx);
            insertTags(toDoBean, idx);
        }

    }

    //태그 입력 메서드
	public void insertTags(ToDo toDoBean, int idx){

		List<String> tags = Arrays.asList(toDoBean.getTags().trim().split(" "));

		for(String tag : tags) {
			if(!tag.equals("")) {
				Tag tagBean = new Tag();
				tagBean.setToDoIdx(idx);
				tagBean.setTag(tag);
                log.info("태그 입력 " + tagBean.getTag());
				sqlSession.insert(namespaceForToDoList + "insertTag", tagBean);
			}
		}

	}




}
