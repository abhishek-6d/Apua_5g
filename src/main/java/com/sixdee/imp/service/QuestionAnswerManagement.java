package com.sixdee.imp.service;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.dto.FAQQADetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.FAQDetailInfo;
import com.sixdee.imp.service.serviceDTO.resp.FAQDetailsDTO;


public class QuestionAnswerManagement
{
	public FAQDetailInfo getFAQuestion()
	{
		FAQDetailInfo detailInfo = new FAQDetailInfo();
		
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		
		GenericDTO genericDTO = adapter.callFeature("FAQDETAILS", "");
		
		com.sixdee.imp.dto.FAQDetailsDTO faqDTO = (com.sixdee.imp.dto.FAQDetailsDTO)genericDTO.getObj();
		System.out.println("------------in service size ---------- "+faqDTO.getFAQList().size());
		
		FAQDetailsDTO[] all = new FAQDetailsDTO[faqDTO.getFAQList().size()];
		for(int i = 0 ; i <faqDTO.getFAQList().size() ; i++)
		{
			FAQQADetailsDTO dto =  (FAQQADetailsDTO) faqDTO.getFAQList().get(i);
			all[i] = new FAQDetailsDTO();
			all[i].setQuestion(dto.getQuestion());
			all[i].setAnswer(dto.getAnswer());
		}
		
		/*FAQDetailsDTO[] all = new FAQDetailsDTO[1];
		all[0] = new FAQDetailsDTO();
		all[0].setQuestion("LMS?");
		all[0].setAnswer("Loyalty Management System");
		*/
		detailInfo.setFaQuestion(all);
		
		return detailInfo;
	}
}
