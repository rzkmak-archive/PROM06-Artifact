import torch
from scipy.special import softmax
from transformers import AutoTokenizer, AutoModelForSequenceClassification

BERT_MODEL = "socialmediaie/TRAC2020_ALL_C_bert-base-multilingual-uncased"
TASK_LABELS = ["OAG-GEN", "OAG-NGEN", "NAG-GEN", "NAG-NGEN", "CAG-GEN", "CAG-NGEN"]


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

        label_prob_dict = dict(zip(TASK_LABELS, preds_probs[0]))
        max_prob = max(label_prob_dict.values())
        highest_prob_label = [label for label, prob in label_prob_dict.items() if prob == max_prob][0]

        print(highest_prob_label)
