package org.ic.tennistrader.service.threads.file_readers;

public enum RecordedDataFormat {
	FRACSOFT_SIMPLE(19), FRACSOFT_FULL(23), RECORDED_NO_TOTAL_MATCHED(22); 
	
	private int nElements;
	
	RecordedDataFormat(int nElements){
		this.nElements = nElements;
	}
	
	public int getNElements(){
		return this.nElements;
	}
}
