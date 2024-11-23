package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.LoneDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface LoneModelInt {
	public long add(LoneDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(LoneDTO dto)throws ApplicationException;
	public void update(LoneDTO dto)throws ApplicationException,DuplicateRecordException;
	public List list()throws ApplicationException;
	public List list(int pageNo,int pageSize)throws ApplicationException;
	public List search(LoneDTO dto)throws ApplicationException;
	public List search(LoneDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public LoneDTO findByPK(long pk)throws ApplicationException;
	public LoneDTO fingByName(String name)throws ApplicationException;


}
