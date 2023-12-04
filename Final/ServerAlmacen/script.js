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
    if(localStorage.getItem('productList') == null){
        productList = [];
    }
    else{
        productList = JSON.parse(localStorage.getItem('productList'));
    }

    var html="";
    productList.forEach(function(product, index){
        html += `<tr>
                    <td>${index+1}</td>
                    <td>${product.name}</td>
                    <td>${product.category}</td>
                    <td>${product.amount}</td>
                    <td>${product.cost}</td>
                    <td><button onclick="deleteProduct(${index})">Delete</button></td>
                </tr>`;
    }); 
    
}