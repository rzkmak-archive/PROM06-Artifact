import tensorflow as tf
import numpy as np
import torch
from scipy.special import softmax
from transformers import AutoTokenizer, AutoModelForSequenceClassification

BERT_MODEL = "socialmediaie/TRAC2020_ALL_C_bert-base-multilingual-uncased"
TASK_LABEL_IDS = {
    "Sub-task A": ["OAG", "NAG", "CAG"],
    "Sub-task B": ["GEN", "NGEN"],
    "Sub-task C": ["OAG-GEN", "OAG-NGEN", "NAG-GEN", "NAG-NGEN", "CAG-GEN", "CAG-NGEN"]
}


class CyberBullyDetectionService:
    def __init__(self):
        self.model = AutoModelForSequenceClassification.from_pretrained(BERT_MODEL)
        self.tokenizer = AutoTokenizer.from_pretrained(BERT_MODEL)

    def classify(self, sentence):
        self.model.eval()
        processed_sentence = f"{self.tokenizer.cls_token} {sentence}"
        tokens = self.tokenizer.tokenize(processed_sentence)
        indexed_tokens = self.tokenizer.convert_tokens_to_ids(tokens)
        tokens_tensor = torch.tensor([indexed_tokens])

        with torch.no_grad():
            logits = self.model(tokens_tensor, labels=None).logits

        preds = logits.detach().cpu().numpy()
        preds_probs = softmax(preds, axis=1)
        preds = np.argmax(preds_probs, axis=1)

        task_labels = TASK_LABEL_IDS["Sub-task C"]
        preds_labels = np.array(task_labels)[preds]
        print(dict(zip(task_labels, preds_probs[0])), preds_labels)
