from fastapi import FastAPI

app = FastAPI()

@app.get("/health")
def health_check():
    return {"status": "analysis service is running"}
