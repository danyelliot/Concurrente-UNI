function validateForm() {
  var name = document.getElementById("name").value;
  var category = document.getElementById("category").value;
  var amount = document.getElementById("amount").value;
  var cost = document.getElementById("cost").value;

  if (name == "") {
    alert("Name must be filled out");
    return false;
  }
  
  if (category == "") {
    alert("Category must be filled out");
    return false;
  }
  if (amount == "") {
    alert("Amount must be filled out");
    return false;
  }
  else if (amount < 0) {
    alert("Amount must be positive");
    return false;
  }

  if (cost == "") {
     alert("Cost must be filled out");
     return false;
  }
  else if (cost < 0) {
     alert("Cost must be positive");
     return false;
    }

  return true;
}

function showData(){
    var productList;
    if(localStorage.getItem("productList") == null){
        productList = [];
    }
    else{
        productList = JSON.parse(localStorage.getItem("productList"));
    }

    var html="";
    var index = 0;
    productList.forEach(function(product, index){
        index = index + 1;
        html += "<tr>";
        html += "<td>" + (index) + "</td>";
        html += "<td>" + product.name + "</td>";
        html += "<td>" + product.category + "</td>";   
        html += "<td>" + product.amount + "</td>";
        html += "<td>" + product.cost + "</td>";
        //html += 
        //    '<td><button class="btn btn-danger" onclick="deleteProduct('+index+')">Delete</button><button class="btn btn-warning m-2" onclick="updateProduct('+index+')">Edit</button></td>';
        html += '<td class="d-flex align-items-center justify-content-center flex-row flex-wrap">' +
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
    if(validateForm() == true){
        var name = document.getElementById("name").value;
        var category = document.getElementById("category").value;   
        var amount = document.getElementById("amount").value;   
        var cost = document.getElementById("cost").value;   

        var productList;
        if(localStorage.getItem("productList") == null){
            productList = [];
        } else {
            productList = JSON.parse(localStorage.getItem("productList"));
        }

        productList.push({
            name: name,
            category: category,
            amount: amount,
            cost: cost
        });

        localStorage.setItem("productList", JSON.stringify(productList));
        showData();

        document.getElementById("name").value = "";
        document.getElementById("category").value = "";
        document.getElementById("amount").value = "";
        document.getElementById("cost").value = "";

    }
}

function deleteProduct(index){
    var productList;
    if(localStorage.getItem("productList") == null){
        productList = [];
    }
    else{
        productList = JSON.parse(localStorage.getItem("productList"));
    }
    productList.splice(index-1, 1);
    localStorage.setItem("productList", JSON.stringify(productList));
    showData();
}

function updateProduct(index){
    index = index - 1;
    document.getElementById("Submit").style.display = "none";
    document.getElementById("Update").style.display = "block";

    var productList;
    if(localStorage.getItem("productList") == null){
        productList = [];
    } else {
        productList = JSON.parse(localStorage.getItem("productList"));
    }

    document.getElementById("name").value = productList[index].name;
    document.getElementById("category").value = productList[index].category;
    document.getElementById("amount").value = productList[index].amount;    
    document.getElementById("cost").value = productList[index].cost;

    document.querySelector("#Update").onclick = function(){
        if(validateForm() == true){
            productList[index].name = document.getElementById("name").value;
            productList[index].category = document.getElementById("category").value;
            productList[index].amount = document.getElementById("amount").value;
            productList[index].cost = document.getElementById("cost").value;

            localStorage.setItem("productList", JSON.stringify(productList));

            showData();

            document.getElementById("name").value = "";
            document.getElementById("category").value = "";
            document.getElementById("amount").value = "";
            document.getElementById("cost").value = "";

            document.getElementById("Submit").style.display = "block";
            document.getElementById("Update").style.display = "none";
        }
    }
}
