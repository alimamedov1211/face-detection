let bool = false

function startWebCam() {

    const video = document.getElementById('video')

    Promise.all([
        faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
        faceapi.nets.faceLandmark68Net.loadFromUri('/models'),
        faceapi.nets.faceRecognitionNet.loadFromUri('/models'),
        faceapi.nets.faceExpressionNet.loadFromUri('/models')
    ]).then(startVideo)

    function startVideo() {
        navigator.getUserMedia({ video: {} },
            stream => video.srcObject = stream,
            err => console.error(err)
        )
    }

    video.addEventListener('play', () => {
        const canvas = faceapi.createCanvasFromMedia(video)
        document.getElementById('videoContainer').append(canvas)
        const displaySize = { width: video.width, height: video.height }
        faceapi.matchDimensions(canvas, displaySize)
        setInterval(async() => {
            const detections = await faceapi.detectAllFaces(video, new faceapi.TinyFaceDetectorOptions()).withFaceLandmarks().withFaceExpressions()
            const resizedDetections = faceapi.resizeResults(detections, displaySize)
            canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height)
            faceapi.draw.drawDetections(canvas, resizedDetections)
            faceapi.draw.drawFaceLandmarks(canvas, resizedDetections)
            faceapi.draw.drawFaceExpressions(canvas, resizedDetections)
        }, 100)
    })
    document.getElementById('webcamBtn').style.display = 'none'
}

// Set config defaults when creating the instance
const instance = axios.create({
    baseURL: 'http://localhost:8099/api'
});

// Add a request interceptor
instance.interceptors.request.use(function(config) {
    // Do something before request is sent
    config.headers = Object.assign({
        "Content-Type": "multipart/formdata",
    }, config.headers);

    return config;
}, function(error) {
    // Do something with request error
    return Promise.reject(error);
});

// Add a response interceptor
instance.interceptors.response.use(function(response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    return response;
}, function(error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
});


const submitForm = document.querySelector('#submitBtn');

submitBtn.addEventListener('click', function(event) {


    event.preventDefault();

    let convertedImage = document.getElementById('convertedImage').files[0];
    let form = new FormData();
    form.append('image', convertedImage);

    let url = 'http://localhost:8099/api/base';

    axios.post(url, form)
        .then(function(response) {
            fillGender(response.data);
        })
        .catch(function(response) {
            console.log(response);
        });
})

function imageUploadedEvent(img) {
    localStorage.clear();
    let value = document.getElementById('urlWritten');
    encodeImageFileAsURL(img);
}



function fillGender(gender) {
    let elem = document.querySelector('#gender');
    elem.innerHTML += gender.gender + `( accuracy:${gender.accuracy})`;
    elem.style.display = 'block';
}



function getResponseFromApi() {

    let result;
    let convertedImage = document.getElementById('convertedImage').files[0];
    let form = new FormData();
    form.append('image', convertedImage);

    postData('http://localhost:8099/api/base', form);
}



function postData(url, data) {
    axios.post(url, data)
        .then(function(response) {
            console.log(response);
        })
        .catch(function(response) {
            console.log(response);
        });

}





function encodeImageFileAsURL(element) {
    let imgElem = document.getElementById('image');
    imgElem.style.display = 'inline-block'
    let base64 = localStorage.getItem('base64')
    imgElem.style.maxWidth = '500px'
    document.getElementById('fileUploaded').style.display = 'none'
    bool = true;
    let file = element.files[0];
    let reader = new FileReader();
    reader.onloadend = function() {
        localStorage.setItem('base64', reader.result)
        imgElem.src = reader.result;
    }
    reader.readAsDataURL(file);
    // getResponseFromApi();
}