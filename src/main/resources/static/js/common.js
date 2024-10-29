// Exibe o loader
function showLoader() {
    document.getElementById('loader-overlay').style.display = 'flex';
}

// Esconde o loader
function hideLoader() {
    document.getElementById('loader-overlay').style.display = 'none';
}

// Função para inicializar o comportamento do checkbox "Selecionar todos"
function initSelectAllCheckbox(selectAllId, checkboxName, toggleCallback) {
    const selectAllCheckbox = document.getElementById(selectAllId);
    if (selectAllCheckbox) {
        selectAllCheckbox.onclick = function() {
            toggleCheckboxes(selectAllCheckbox.checked, checkboxName);
            toggleCallback();
        };
    }
}

// Função para selecionar ou desmarcar todos os checkboxes ativos
function toggleCheckboxes(checkedState, checkboxName) {
    const checkboxes = document.getElementsByName(checkboxName);
    checkboxes.forEach(function(checkbox) {
        if (!checkbox.disabled) {
            checkbox.checked = checkedState;
        }
    });
}

// Função para verificar se algum checkbox está selecionado e atualizar a visibilidade do botão de inativação
function toggleInativarButton(buttonId, selectedItems) {
    const inativarButton = document.getElementById(buttonId);
    inativarButton.style.display = selectedItems.length > 0 ? 'inline-block' : 'none';
}

// Função para desabilitar o checkbox "Selecionar todos" se todos os itens estiverem inativos
function disableSelectAllIfAllInactive(checkboxName, selectAllId) {
    const itemCheckboxes = document.getElementsByName(checkboxName);
    const allInactive = Array.from(itemCheckboxes).every(checkbox => checkbox.disabled);
    const selectAllCheckbox = document.getElementById(selectAllId);
    selectAllCheckbox.disabled = allInactive;
}

// Função para enviar requisição de ativação ou inativação
function processRequest(ids, method, url, successMessage, errorMessage) {
    showLoader();
    fetch(`${url}${ids.join(',')}`, {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (response.ok) {
            alert(successMessage);
            location.reload();
        } else {
            alert(errorMessage);
        }
    })
    .catch(error => {
        console.error(errorMessage, error);
    });
}

// Função para pegar itens selecionados
function getSelectedItems(checkboxName) {
    return Array.from(document.querySelectorAll(`input[name="${checkboxName}"]:checked`)).map(item => item.value);
}


function submitFormWithLoader() {
    showLoader(); // Exibe o loader
    document.getElementById('filtroInativosForm').submit(); // Submete o formulário
}
