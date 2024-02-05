from service.cyberbully_detection_service import CyberBullyDetectionService
from typing import Union
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def sample():
    service = CyberBullyDetectionService()
    return service.classify("This is a good cat and this is a bad dog.")