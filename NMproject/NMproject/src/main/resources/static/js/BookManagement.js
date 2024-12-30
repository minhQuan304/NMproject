// Thêm vào đầu file
window.initializeUI = initializeUI;
window.fetchBooks = fetchBooks;
window.updateTableDisplay = updateTableDisplay;
const body = document.getElementById("body");
function notification(content, isSuccess){
    const notification = document.getElementById('notification');
    if(isSuccess){
        notification.innerHTML = `
            <ion-icon name="checkmark-outline"></ion-icon> ${content}
        `;
        notification.style.backgroundColor = `#41eea0`;
        notification.style.color = 'black';
    } else {
        notification.innerHTML = `
            <ion-icon name="alert-circle-outline"></ion-icon> ${content}
        `;
        notification.style.backgroundColor = `red`;
        notification.style.color = '#ededed';
    }
    setTimeout(() => {
        notification.classList.add('show');
    }, 0);
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3500); 
}
// show/hidden
const showMenu = (toggleId, navbarId, bodyId) => {
    const toggle = document.getElementById(toggleId),
        navbar = document.getElementById(navbarId),
        bodypadding = document.getElementById(bodyId);
    if (toggle && navbar) {
        toggle.addEventListener('click', () => {
            navbar.classList.toggle('show');
            toggle.classList.toggle('rotate');
            bodypadding.classList.toggle('expander')
        });
    }
};
showMenu('nav_toggle', 'navbar', 'body');
// color hover of navbar
const linkColor = document.querySelectorAll('.nav_link');
function colorLink() {
    linkColor.forEach(link => link.classList.remove('active'));
    this.classList.add('active');
}
linkColor.forEach(link => link.addEventListener('click', colorLink));
// dropdown menu profile
const dropDownProfile = (menuId, userpicId) => {
    const userpic = document.getElementById(userpicId),
        menuProfile = document.getElementById(menuId);
    if (userpic && menuProfile) {
        userpic.addEventListener('click', (e) => {
            e.stopPropagation();
            menuProfile.classList.toggle('active');
        });
        window.addEventListener('click', (e) => {
            if (!menuProfile.contains(e.target) && !userpic.contains(e.target)) {
                menuProfile.classList.remove('active');
            }
        });
    }
};
dropDownProfile('drop-menu-profile', 'userpic');

// set username from sessionStorage
const infoUser = JSON.parse(sessionStorage.getItem('infoUser'));
console.log(infoUser);
if (infoUser) {
    document.addEventListener('DOMContentLoaded', () => {
        const imgElement = `
            <img src="http://localhost:8081${infoUser.imageLink}" class="user-pic-in">${infoUser.username}
        `
        document.getElementById('username').innerHTML = imgElement;
        // document.getElementById('profile-name').textContent = infoUser.name || "Chưa cập nhật";
        // document.getElementById('profile-phone').textContent = infoUser.phone || "Chưa cập nhật";
        // document.getElementById('profile-address').textContent = infoUser.address || "Chưa cập nhật";
        // document.getElementById('avt-in-profile').src = infoUser.imageLink || "/Assets/user0.png";

    });
};

// Thêm event listener khi trang được load
document.addEventListener("DOMContentLoaded", function () {
  console.log("Page loaded, fetching books...");
  initializeUI();
  fetchBooks();
});

const API_URL = "http://localhost:8081/api/books";

let currentBookId = null;
let currentPage = 1;
let rowsPerPage = 10;
let totalPages = 1;
let allBooks = [];
let filteredBooks = [];

// Sửa lại hàm initializeUI
function initializeUI() {
  console.log("Initializing UI...");
  setupDropdownAndUI();
  initializeSearch();
  // Bỏ fetchBooks() ở đây vì nó sẽ được gọi từ loadContent.js
}

// Setup UI components
function setupDropdownAndUI() {
  const container = document.querySelector(".bg-gray-100");
  if (!container) return;

  // Cập nhật header với dropdown
  const header = container.querySelector(".flex.justify-between");
  if (header) {
    const leftSection = header.querySelector(".flex.items-center");
    if (leftSection) {
      // Cập nhật select cho số dòng
      const rowsSelect = leftSection.querySelector("#rowsPerPage");
      if (rowsSelect) {
        rowsSelect.innerHTML = `
          <option value="5">5 dòng</option>
          <option value="10" selected>10 dòng</option>
          <option value="20">20 dòng</option>
        `;

        rowsSelect.addEventListener("change", (e) => {
          rowsPerPage = parseInt(e.target.value);
          currentPage = 1;
          updateTableDisplay();
        });
      }

      // Thêm nút dropdown với menu
      const dropdownButton = document.createElement("div");
      dropdownButton.innerHTML = `
        <div class="relative">
              <button 
                class="bg-green-500 text-white p-3 rounded-full hover:bg-yellow-500 transition duration-400"
                id="bookOptionsButton"
              >
                <i class="fa fa-book-open animate-bounce text-base"></i>
              </button>
          <div 
            id="bookDropdownMenu" 
            class="hidden absolute left-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50"
          >
            <a href="#" onclick="showAddModal()" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <i class="fa fa-plus mr-2"></i>Thêm sách mới
            </a>
            <a href="#" onclick="showFilterOptions()" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <i class="fa fa-filter mr-2"></i>Lọc sách
            </a>
          </div>
        </div>
      `;
      leftSection.appendChild(dropdownButton);

      // Thêm x lý click cho dropdown
      const button = dropdownButton.querySelector("#bookOptionsButton");
      const menu = dropdownButton.querySelector("#bookDropdownMenu");

      button.addEventListener("click", (e) => {
        e.stopPropagation();
        menu.classList.toggle("hidden");
      });

      // Đóng dropdown khi click ra ngoài
      document.addEventListener("click", (e) => {
        if (!menu.contains(e.target) && !button.contains(e.target)) {
          menu.classList.add("hidden");
        }
      });
    }
  }

  // Load Font Awesome nếu chưa có
  if (!document.querySelector('link[href*="font-awesome"]')) {
    const fontAwesome = document.createElement("link");
    fontAwesome.rel = "stylesheet";
    fontAwesome.href =
      "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css";
    document.head.appendChild(fontAwesome);
  }
}

// Lấy danh sách sách từ server
function fetchBooks() {
  console.log("Fetching books from:", API_URL);
  fetch(`${API_URL}/allbooks`)
    .then((response) => {
      console.log("Response status:", response.status);
      if (!response.ok) throw new Error("Không thể kết nối với server");
      return response.json();
    })
    .then((books) => {
      console.log("Received books:", books);
      allBooks = books;
      filteredBooks = [];
      updateTableDisplay();
    })
    .catch((error) => {
      console.error("Error fetching books:", error);
      alert("Không thể tải danh sách sách: " + error.message);
    });
}

// Hàm cập nhật hiển thị bảng
function updateTableDisplay() {
  const startIndex = (currentPage - 1) * rowsPerPage;
  const booksToShow = filteredBooks.length > 0 ? filteredBooks : allBooks;
  const endIndex = Math.min(startIndex + rowsPerPage, booksToShow.length);
  const displayedBooks = booksToShow.slice(startIndex, endIndex);

  const tableBody = document.querySelector("#bookTable tbody");
  tableBody.innerHTML = "";

  if (displayedBooks.length === 0) {
    // Xóa nội dung bảng khi không có kết quả
    tableBody.innerHTML = "";

    // Cập nhật thông tin phân trang
    document.getElementById("startRow").textContent = "0";
    document.getElementById("endRow").textContent = "0";
    document.getElementById("totalRows").textContent = "0";

    // Xóa các nút phân trang
    document.getElementById("pageNumbers").innerHTML = "";

    // Disable nút prev/next
    document.getElementById("prevPage").disabled = true;
    document.getElementById("nextPage").disabled = true;

    return;
  }

  displayedBooks.forEach((book, index) => {
    const row = document.createElement("tr");
    row.className = "hover:bg-gray-50 cursor-pointer";
    row.innerHTML = `
      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        ${startIndex + index + 1}
      </td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        ${book.title}
      </td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        ${book.author}
      </td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        ${book.category}
      </td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        ${book.quantityValid}/${book.quantityTotal}
      </td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        ${formatDate(book.publishDate)}
      </td>
    `;
    row.addEventListener("click", () => showBookDetail(book.bookID));
    tableBody.appendChild(row);
  });

  updatePagination(booksToShow.length);
}

// Hàm cập nhật thông tin phân trang
function updatePagination(totalBooks) {
  totalPages = Math.ceil(totalBooks / rowsPerPage);
  const startRow = (currentPage - 1) * rowsPerPage + 1;
  const endRow = Math.min(currentPage * rowsPerPage, totalBooks);

  document.getElementById("startRow").textContent = totalBooks ? startRow : 0;
  document.getElementById("endRow").textContent = endRow;
  document.getElementById("totalRows").textContent = totalBooks;

  const pageNumbers = document.getElementById("pageNumbers");
  pageNumbers.innerHTML = "";

  // Hiển thị các nút số trang
  for (let i = 1; i <= totalPages; i++) {
    const pageButton = document.createElement("button");
    pageButton.className = `px-3 py-1 rounded-md ${
      i === currentPage
        ? "bg-green-500 text-white"
        : "bg-gray-200 text-gray-700 hover:bg-gray-300"
    }`;
    pageButton.textContent = i;
    pageButton.addEventListener("click", () => {
      currentPage = i;
      updateTableDisplay();
    });
    pageNumbers.appendChild(pageButton);
  }

  // Cập nhật trạng thái nút Trước/Sau
  document.getElementById("prevPage").disabled = currentPage === 1;
  document.getElementById("nextPage").disabled = currentPage === totalPages;
}

// Thêm event listeners cho các nút điều hướng
document.getElementById("prevPage").addEventListener("click", () => {
  if (currentPage > 1) {
    currentPage--;
    updateTableDisplay();
  }
});

document.getElementById("nextPage").addEventListener("click", () => {
  if (currentPage < totalPages) {
    currentPage++;
    updateTableDisplay();
  }
});

// Thêm event listener cho dropdown chọn số dòng
document.getElementById("rowsPerPage").addEventListener("change", (e) => {
  rowsPerPage = parseInt(e.target.value);
  currentPage = 1; // Reset về trang đầu tiên
  updateTableDisplay();
});

// Thêm các hàm xử lý modal chi tiết
function showBookDetail(id) {
  currentBookId = id;
  fetch(`${API_URL}/detail/${id}`)
    .then((response) => response.json())
    .then((book) => {
      const detailContent = document.querySelector(".book-detail-content");
      detailContent.innerHTML = `
        <div class="grid grid-cols-2 gap-4">
          <div class="col-span-2 flex justify-left">
            <img src="${API_URL}${book.imageLink}" 
                 alt="${book.title || book.name}" 
                 class="h-48 w-48 object-cover rounded-lg">
          </div>
          <div class="font-medium text-gray-500">Mã sách:</div>
          <div>${book.bookID || book.id}</div>
          <div class="font-medium text-gray-500">Tên sách:</div>
          <div>${book.title || book.name}</div>
          <div class="font-medium text-gray-500">Tác giả:</div>
          <div>${book.author}</div>
          <div class="font-medium text-gray-500">Thể loại:</div>
          <div>${book.category}</div>
          <div class="font-medium text-gray-500">Ngày xuất bản:</div>
          <div>${formatDate(book.publishDate || book.publishDate)}</div>
          <div class="font-medium text-gray-500">Tổng số lượng:</div>
          <div>${book.quantityTotal || book.totalQuantity}</div>
          <div class="font-medium text-gray-500">Số lượng còn lại:</div>
          <div>${book.quantityValid || book.availableQuantity}</div>
          <div class="font-medium text-gray-500">Đánh giá:</div>
          <div>${renderRating(book.rate || 0)}</div>
          <div class="font-medium text-gray-500">Mô tả:</div>
          <div class="col-span-2">${book.description || "Không có mô tả"}</div>
        </div>
      `;
      showModal("detailModal");
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Không thể tải thông tin sách");
    });
}

function closeDetailModal() {
  closeModal("detailModal");
  currentBookId = null;
}

function handleDelete() {
  if (currentBookId) {
    deleteBook(currentBookId);
  }
}

// Xử lý thêm sách mới

document.getElementById("bookForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const formData = new FormData();
    const imageFile = document.getElementById("imageFile").files[0];

    console.log(imageFile);

    if (imageFile) {
        formData.append("image", imageFile); // Đúng tên tham số
    } else {
        alert("Vui lòng chọn ảnh cho sách!");
        return;
    }

    const newBook = {
        title: document.getElementById("title").value.trim(),
        author: document.getElementById("author").value.trim(),
        category: document.getElementById("category").value.trim(),
        description: document.getElementById("description").value.trim() || null,
        publishDate: document.getElementById("date").value.trim(),
        quantityTotal: parseInt(document.getElementById("quantityTotal").value),
        quantityValid: parseInt(document.getElementById("quantityValid").value),
    };

    if (newBook.quantityValid > newBook.quantityTotal) {
        alert("Số lượng còn lại không thể lớn hơn tổng số lượng!");
        return;
    }

    if (
        !newBook.title ||
        !newBook.author ||
        !newBook.category ||
        !newBook.publishDate ||
        isNaN(newBook.quantityTotal) ||
        isNaN(newBook.quantityValid)
    ) {
        alert("Vui lòng điền đầy đủ thông tin và kiểm tra số lượng!");
        return;
    }

    formData.append("book", JSON.stringify(newBook)); // Thêm JSON đúng tham số

    try {
        const response = await fetch(`${API_URL}/addbooks`, {
            method: "POST",
            body: formData,
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Backend error:", errorText);
            alert(`Lỗi: ${response.status} - ${errorText}`);
            return;
        }

        const data = await response.json();
        alert("Thêm sách thành công!");
        this.reset();
        fetchBooks();
        closeAddModal();
    } catch (error) {
        console.error("Error:", error);
        alert(error.message || "Lỗi khi thêm sách");
    }
});

	


// Thêm các hàm xử lý modal
function showUpdateModal(id) {
  closeDetailModal();
  fetch(`${API_URL}/detail/${id}`)
    .then((response) => {
      if (!response.ok) throw new Error("Không thể lấy thông tin sách");
      return response.json();
    })
    .then((book) => {
      document.getElementById("updateBookId").value = book.bookID;
      document.getElementById("updateName").value = book.title;
      document.getElementById("updateAuthor").value = book.author;
      document.getElementById("updateCategory").value = book.category;
      document.getElementById("updateDescription").value =
        book.description || "";
      document.getElementById("updateDate").value = book.publishDate;
      document.getElementById("updateTotalQuantity").value = book.quantityTotal;
      document.getElementById("updateAvailableQuantity").value =
        book.quantityValid;

      // Hiển thị ảnh hiện tại
      const currentImage = document.getElementById("currentBookImage");
      currentImage.src = `${API_URL}${book.imageLink}`;

      showModal("updateModal");
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Lỗi khi lấy thông tin sách: " + error.message);
    });
}

function closeUpdateModal() {
  closeModal("updateModal");
}

// Sửa lại hàm updateBook
function updateBook(id) {
  console.log("updateBook called with id:", id);
  showUpdateModal(id);
}

// Thêm xử l form cập nhật
document.getElementById("updateForm").addEventListener("submit", async function (e) {
  e.preventDefault();
  const id = document.getElementById("updateBookId").value.trim(); // Lấy id sách từ input ẩn hoặc nguồn khác

  const formData = new FormData();
  const imageFile = document.getElementById("updateImageFile").files[0]; // Lấy file ảnh từ input

  if (imageFile) {
    formData.append("file", imageFile); // Thêm file ảnh vào FormData
  }

  // Chuẩn bị dữ liệu sách cần cập nhật
  const updatedBook = {
    title: document.getElementById("updateName").value.trim(),
    author: document.getElementById("updateAuthor").value.trim(),
    category: document.getElementById("updateCategory").value.trim(),
    description: document.getElementById("updateDescription").value.trim() || null,
    publishDate: document.getElementById("updateDate").value.trim(),
    quantityTotal: parseInt(document.getElementById("updateTotalQuantity").value),
    quantityValid: parseInt(document.getElementById("updateAvailableQuantity").value),
  };

  // Kiểm tra logic số lượng
  if (updatedBook.quantityValid > updatedBook.quantityTotal) {
    alert("Số lượng còn lại không thể lớn hơn tổng số lượng!");
    return;
  }

  // Thêm thông tin sách vào FormData
  formData.append("bookDTO", JSON.stringify(updatedBook));

  try {
    const response = await fetch(`${API_URL}/update/${id}`, {
      method: "PUT",
      body: formData,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Lỗi ${response.status}: ${errorText}`);
    }

    const data = await response.json();
    alert("Cập nhật sách thành công!");
    closeUpdateModal();
    fetchBooks(); // Cập nhật lại danh sách sách
  } catch (error) {
    console.error("Error:", error);
    alert("Lỗi khi cập nhật sách: " + error.message);
  }
});



// Hàm xóa sách
function deleteBook(id) {
  if (!confirm("Bạn có chắc muốn xóa sách này?")) return;

  fetch(`${API_URL}/delete/${id}`, {
    method: "DELETE",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Lỗi khi xóa sách");
      }
      // Không cần đợi response.json() vì DELETE thường không trả về dữ liệu
      alert("Xóa sách thành công!");
      closeDetailModal(); // Đóng modal chi tiết sau khi xóa
      fetchBooks(); // Cập nhật lại danh sách
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Lỗi khi xóa sách: " + error.message);
    });
}

// Thêm các hàm xử lý modal thêm sách
function showAddModal() {
  showModal("addModal");
}

function closeAddModal() {
  closeModal("addModal");
  document.getElementById("bookForm").reset();
}

// Thêm các hàm xử lý dropdown
function toggleDropdown() {
  document.getElementById("bookDropdown").classList.toggle("show");
}

// Đóng dropdown khi click ra ngoài
window.onclick = function (event) {
  if (
    !event.target.matches(".fa-book-open") &&
    !event.target.closest(".dropdown button")
  ) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    for (var i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains("show")) {
        openDropdown.classList.remove("show");
      }
    }
  }
};

function showModal(modalId) {
  const modal = document.getElementById(modalId);
  modal.classList.remove("hidden");
  // Đảm bảo animation chạy mỗi lần hiển thị
  const modalContent = modal.querySelector("div");
  modalContent.style.animation = "none";
  modalContent.offsetHeight; // Trigger reflow
  modalContent.style.animation = null;
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  modal.classList.add("hidden");
}

// Thêm hàm để hiển th rating
function renderRating(rate) {
  const fullStars = Math.floor(rate);
  const hasHalfStar = rate % 1 !== 0;
  let html = "";

  for (let i = 0; i < fullStars; i++) {
    html += '<i class="fas fa-star text-yellow-400"></i>';
  }
  if (hasHalfStar) {
    html += '<i class="fas fa-star-half-alt text-yellow-400"></i>';
  }
  const emptyStars = 5 - Math.ceil(rate);
  for (let i = 0; i < emptyStars; i++) {
    html += '<i class="far fa-star text-yellow-400"></i>';
  }

  return html;
}

// Thêm hàm format date
function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString("vi-VN");
}