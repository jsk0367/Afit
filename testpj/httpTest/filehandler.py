from pathlib import Path
import os

home = os.path.abspath(os.path.dirname(__name__))

def handle_uploaded_file(f):
    try:
        with open(home + '/media/'+f.name, 'wb+') as destination:
            for chunk in f.chunks():
                destination.write(chunk)
        return True
    except Exception as e:
        print("error:", e.__class__, "occured.");
        return False
