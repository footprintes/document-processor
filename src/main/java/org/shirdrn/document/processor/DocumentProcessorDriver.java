package org.shirdrn.document.processor;

import org.shirdrn.document.processor.common.Component;
import org.shirdrn.document.processor.common.Context;
import org.shirdrn.document.processor.component.BasicContextInitializer;
import org.shirdrn.document.processor.component.DenoisingDocumentTerms;
import org.shirdrn.document.processor.component.DocumentTFIDFComputation;
import org.shirdrn.document.processor.component.test.CollectingTestDocumentWords;
import org.shirdrn.document.processor.component.test.OutputtingQuantizedTestData;
import org.shirdrn.document.processor.component.train.CollectingTrainDocumentWords;
import org.shirdrn.document.processor.component.train.OutputtingQuantizedTrainData;

public class DocumentProcessorDriver {

	public static void main(String[] args) {
		Context context = new Context();
		boolean isTrainOpen = 
				context.getConfiguration().getBoolean("processor.dataset.train.isopen", false);
		Component[] chain = null;
		if(isTrainOpen) {
			// for train data
			chain = new Component[] {
					new BasicContextInitializer(context),
					new CollectingTrainDocumentWords(context),
					new DocumentTFIDFComputation(context),
					new DenoisingDocumentTerms(context),
					new OutputtingQuantizedTrainData(context)
				};
			prepareAndRun(chain);
		} else {
			// for test data
			chain = new Component[] {
					new BasicContextInitializer(context),
					new CollectingTestDocumentWords(context),
					new DocumentTFIDFComputation(context),
					new DenoisingDocumentTerms(context),
					new OutputtingQuantizedTestData(context)
				};
			prepareAndRun(chain);
		}
		
	}

	private static void prepareAndRun(Component[] chain) {
		for (int i = 0; i < chain.length - 1; i++) {
			Component current = chain[i];
			Component next = chain[i + 1];
			current.setNext(next);
		}
		
		for (Component componennt : chain) {
			componennt.fire();
		}
	}

}
