from fastapi.security import HTTPBasic, HTTPBasicCredentials

from service.cyberbully_detection_service import CyberBullyDetectionService
from fastapi import FastAPI, Depends
from pydantic import BaseModel
from environs import Env

app = FastAPI()
service = CyberBullyDetectionService()
security = HTTPBasic()
env = Env()
env.read_env(".env")


class ScreeningTwitterRequest(BaseModel):
    value: str


def validate_basic_auth(credentials: HTTPBasicCredentials = Depends(security)):
    username = credentials.username
    password = credentials.password

    if username != env.str('APP_KEY') or password != env.str('APP_SECRET'):
        raise Exception("Invalid credentials")

    return username


@app.post("/v1/screening/twitter", dependencies=[Depends(validate_basic_auth)])
def screening_twitter(request: ScreeningTwitterRequest):
    return {"result": service.classify(request)}
