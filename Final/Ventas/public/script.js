function validateSaleForm() {
    var ruc = document.getElementById("ruc").value;
    var name = document.getElementById("name").value;

    if (name == "") {
      alert("Name must be filled out");
      return false;
    }
    
    if (ruc == "") {
        alert("RUC must be filled out");
        return false;
      }
    else if(ruc.length != 11){
        alert("RUC must be 11 digits");
        return false;
    }
  
    return true;
  }
  
  function showData(){
      var salesList;
      if(localStorage.getItem("salesList") == null){
          salesList = [];
      }
      else{
          salesList = JSON.parse(localStorage.getItem("salesList"));
      }
  
      var html="";
      var index = 0;
      salesList.forEach(function(sales, index){
          index = index + 1;
          html += "<tr>";
          html += "<td>" + (index) + "</td>";
          html += "<td>" + sales.ruc + "</td>";
          html += "<td>" + sales.name + "</td>";
          html += "<td>" + "precio" + "</td>";
          
          //html += 
          //    '<td><button class="btn btn-danger" onclick="deleteProduct('+index+')">Delete</button><button class="btn btn-warning m-2" onclick="updateProduct('+index+')">Edit</button></td>';
          html += '<td class="d-flex align-items-center justify-content-center flex-row flex-wrap">' +
          '<button id="view" class="btn btn-warning me-3" onclick="visualizeProduct(' + index + ')">Ver</button>' +
          '<button id="edit" class="btn btn-warning me-3" onclick="updateProduct(' + index + ')">Edit</button>' +
          '<button id="delete" class="btn btn-danger" onclick="deleteProduct(' + index + ')">Delete</button>' +
          '</td>';
          html += "</tr>";
      }); 
      
      document.querySelector("#crudTable tbody").innerHTML = html;
  
  }
  //Loads all data when document or page loaded
  document.onload = showData();
  
  function AddData(){
      if(validateSaleForm() == true){  
          var ruc = document.getElementById("ruc").value;
          var name = document.getElementById("name").value;
      
          var salesList;
          if(localStorage.getItem("salesList") == null){
              salesList = [];
          } else {
              salesList = JSON.parse(localStorage.getItem("salesList"));
          }
  
          salesList.push({
              ruc: ruc,
              name: name,
          });
  
          localStorage.setItem("salesList", JSON.stringify(salesList));
          showData();
  
          document.getElementById("ruc").value = "";
          document.getElementById("name").value = "";
        
        
  
      }
  }
  
  function deleteProduct(index){
      var salesList;
      if(localStorage.getItem("salesList") == null){
          salesList = [];
      } else {
          salesList = JSON.parse(localStorage.getItem("salesList"));
      }
      salesList.splice(index-1, 1);
      localStorage.setItem("salesList", JSON.stringify(salesList));
      showData();
  }
  
  function updateProduct(index){
      index = index - 1;
      document.getElementById("Submit").style.display = "none";
      document.getElementById("Update").style.display = "block";
  
      var salesList;
      if(localStorage.getItem("salesList") == null){
          salesList = [];
      } else {
          salesList = JSON.parse(localStorage.getItem("salesList"));
      }
      document.getElementById("ruc").value = salesList[index].ruc;
      document.getElementById("name").value = salesList[index].name;

  
      document.querySelector("#Update").onclick = function(){
          if(validateSaleForm() == true){
              salesList[index].ruc = document.getElementById("ruc").value;
              salesList[index].name = document.getElementById("name").value;
            
              localStorage.setItem("salesList", JSON.stringify(salesList));
  
              showData();

              document.getElementById("ruc").value = "";
              document.getElementById("name").value = "";
 
              document.getElementById("Submit").style.display = "block";
              document.getElementById("Update").style.display = "none";
          }
      }
  }
function visualizeProduct(index){
    window.location.href = 'detail.html?id=' + index;
}

