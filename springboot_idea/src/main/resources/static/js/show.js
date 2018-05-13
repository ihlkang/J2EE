function Upload() {
    var file;
    file = $('#fileToUpload').get(0).files[0];
    var formData = new FormData();
    formData.append("file",file);
    $.ajax({
        url : 'http://localhost:8080/upload/do_upload',
        type: 'POST',
        contentType:"multipart/form-data",
        processData : false,
        contentType : false,
        async:false,
        data:formData,
        success : function(data) {
            document.getElementById("num_data").value = data;
        },
        error : function() {
            alert('上传出错');
        }
    })
}


