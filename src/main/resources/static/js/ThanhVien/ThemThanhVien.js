function openFormAdd() {
    document.getElementById("addForm").style.display = "block";
}

function closeFormAdd() {
    document.getElementById("addForm").style.display = "none";
}



function saveAdd() {
    // Lấy phần tử input bằng id

        var personName = document.getElementById('addPersonName');
        var personKhoa = document.getElementById('addPersonKhoa');
        var personNganh = document.getElementById('addPersonNganh');
        var personSDT = document.getElementById('addPersonSDT');
        var personEmail = document.getElementById('addPersonEmail');
        var personPassword = document.getElementById('addPersonPassword');


        if (personName && personKhoa && personNganh && personSDT && personEmail && personPassword) {
                const tenTV = personName.value;
                const khoa = personKhoa.options[personKhoa.selectedIndex].value;
                const nganh = personNganh.options[personNganh.selectedIndex].value;
                const sdt = personSDT.value;
                const email = personEmail.value;
                const password = personPassword.value;


                fetch('/ThanhVien.html?&Ten=' + tenTV + '&Khoa=' + khoa + '&Nganh=' + nganh + '&SDT=' + sdt + '&Email=' + email + '&Password=' + password, {
                    method: 'POST'
        })
        .then(response => {
            if (response.ok) {
                // // Sau khi lưu xong, bạn có thể đóng form bằng cách gọi hàm closeForm()
                closeFormAdd();

                alert("Thiết bị đã được thêm thành công");
                window.location.reload(); // Làm mới trang sau khi hiển thị thông báo

            } else {
                console.error('Lỗi khi thêm thành viên');
            }
        })
        .catch(error => {
            console.error('Lỗi khi gửi yêu cầu: ', error);
        });
    
    }
}
var addButton = document.getElementById('addButton');

// Thêm trình nghe sự kiện cho sự kiện nhấp chuột
addButton.addEventListener('click', function(event) {
    openFormAdd();
});

// Lấy phần tử select của Khoa và Ngành
var khoaSelect = document.getElementById('addPersonKhoa');
var nganhSelect = document.getElementById('addPersonNganh');

// Một đối tượng chứa các ngành tương ứng với mỗi khoa
var nganhOptions = {
    "SP KHXH": ["Địa", "Sử", "Văn"],
    "SP KHTN": ["Lí", "Hóa", "Sinh"],
    "Ngoại Ngữ": ["Anh", "NNA"],
    "QTKD": ["QTKD"],
    "QLGD": ["TLH"],
    "Toán UD": ["Toán"],
    "CNTT": ["CNTT", "KTPM", "HTTT"]
};

// Thêm sự kiện change cho select của Khoa
khoaSelect.addEventListener('change', function() {
    // Xóa các option cũ trong select của Ngành
    nganhSelect.innerHTML = '';

    // Lấy ra giá trị của khoa đã chọn
    var selectedKhoa = khoaSelect.value;

    // Lấy danh sách các ngành tương ứng với khoa đã chọn và thêm chúng vào select của Ngành
    var nganhs = nganhOptions[selectedKhoa];
    nganhs.forEach(function(nganh) {
        var option = document.createElement('option');
        option.textContent = nganh;
        option.value = nganh;
        nganhSelect.appendChild(option);
    });
});

