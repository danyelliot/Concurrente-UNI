function validateDetailForm(){
    var idProduct = document.getElementById("idProduct").value;
    var quantity = document.getElementById("quantity").value;

    if (idProduct == "") {
        alert("ID Product must be filled out");
        return false;
    }
    if (quantity == "") {
        alert("Quantity must be filled out");
        return false;
    } else if(quantity < 1){
        alert("Quantity must be greater than 0");
        return false;
    }
    
    return true;

}

function showDetailData(){
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
        html += "<td><strong>" + (index) + "</strong></td>";
        html += "<td>" + sales.idProduct + "</td>";
        html += "<td>" + "nombre" + "</td>";
        html += "<td>" + "tipo" + "</td>";
        html += "<td>" + sales.quantity + "</td>";
        html += "<td>" + "cost u" + "</td>";
        html += "<td>" + "cost t" + "</td>";
        //html += 
        //    '<td><button class="btn btn-danger" onclick="deleteProduct('+index+')">Delete</button><button class="btn btn-warning m-2" onclick="updateProduct('+index+')">Edit</button></td>';
        html += '<td class="d-flex align-items-center justify-content-center flex-row flex-wrap">' +
        '<button id="edit" class="btn btn-warning me-3" onclick="updateProduct(' + index + ')">Edit</button>' +
        '<button id="delete" class="btn btn-danger" onclick="deleteProduct(' + index + ')">Delete</button>' +
        '</td>';
        html += "</tr>";
    }); 
    
    document.querySelector("#crudTableD tbody").innerHTML = html;
}

  //Loads all data when document or page loaded
  document.onload = showDetailData();
  
  function AddData(){
      if(validateDetailForm() == true){  
          var idProduct = document.getElementById("idProduct").value;
          var quantity = document.getElementById("quantity").value;
      
          var salesList;
          if(localStorage.getItem("salesList") == null){
              salesList = [];
          } else {
              salesList = JSON.parse(localStorage.getItem("salesList"));
          }
  
          salesList.push({
              idProduct: idProduct,
              quantity: quantity,
          });
  
          localStorage.setItem("salesList", JSON.stringify(salesList));
          showDetailData();
  
          document.getElementById("idProduct").value = "";
          document.getElementById("quantity").value = "";
             
  
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
      showDetailData();
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
      document.getElementById("idProduct").value = salesList[index].idProduct;
      document.getElementById("quantity").value = salesList[index].quantity;
      
  
      document.querySelector("#Update").onclick = function(){
          if(validateDetailForm() == true){
              salesList[index].idProduct = document.getElementById("idProduct").value;
              salesList[index].quantity = document.getElementById("quantity").value;
            
              localStorage.setItem("salesList", JSON.stringify(salesList));
  
              showDetailData();

              document.getElementById("idProduct").value = "";
              document.getElementById("quantity").value = "";
 
              document.getElementById("Submit").style.display = "block";
              document.getElementById("Update").style.display = "none";
          }
      }
  }

  function SendBack(){
    window.location.href = "index.html";
  }

