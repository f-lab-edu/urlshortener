package jansegety.urlshortener.repository.memoryrepository;

import java.lang.reflect.Field;

import jansegety.urlshortener.error.exception.reflection.PrivateSettingException;

/**
 * Entity들에서 id setter을 삭제하고서도 메모리 레포지토리에서
 * id 값을 할당할 수 있도록 하기위해서 reflection을 사용합니다.
 */
public final class PrivateSetter {

	public static <T, I> void setId(T target, I newId) {
		Class<? extends Object> targetClass = target.getClass();
		
		try {
			 Field[] fields = targetClass.getDeclaredFields();
			 for(Field field : fields) {
				 if(field.getName().equals("id")) {
					 Field idField = targetClass.getDeclaredField(field.getName());
					 idField.setAccessible(true);
					 idField.set(target, newId); 
				 }
			 }
		} catch(Exception ex) {
			throw new PrivateSettingException(ex);
		}
		
	}
	
}