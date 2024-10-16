window.onload = function() {
    initSelectAllCheckbox();
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive();
};

// Função para inicializar o comportamento do checkbox "Selecionar todos"
function initSelectAllCheckbox() {
    var selectAllCheckbox = document.getElementById('select-all');
    if (selectAllCheckbox) {
        selectAllCheckbox.onclick = function() {
            toggleCheckboxes(selectAllCheckbox.checked); // Seleciona/desmarca apenas clientes ativos
            toggleInativarButton(); // Atualiza o botão de inativação após a mudança
        };
    }
}

// Função para selecionar ou desmarcar todos os checkboxes ativos
function toggleCheckboxes(checkedState) {
    var checkboxes = document.getElementsByName('selectedClientes');
    checkboxes.forEach(function(checkbox) {
        if (!checkbox.disabled) {  // Verifica se o cliente está ativo
            checkbox.checked = checkedState;
        }
    });
}

// Função para inicializar o comportamento do botão "Inativar selecionados"
function initInativarButton() {
    var inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            var selectedClientes = getSelectedClientes();
            if (selectedClientes.length > 0) {
                if (confirm('Tem certeza que deseja inativar os clientes selecionados?')) {
                    processClientRequest(selectedClientes, 'DELETE', '/clientes?idsCliente=');
                }
            } else {
                alert('Nenhum cliente foi selecionado.');
            }
        };
    }
}

// Função para pegar os clientes selecionados
function getSelectedClientes() {
    return Array.from(document.querySelectorAll('input[name="selectedClientes"]:checked')).map(function(cliente) {
        return cliente.value;
    });
}

// Função para enviar requisição de ativação ou inativação
function processClientRequest(ids, method, url) {
    fetch(`${url}${ids.join(',')}`, {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (response.ok) {
            alert(method === 'PUT' ? 'Cliente ativado com sucesso.' : 'Clientes inativados com sucesso.');
            location.reload(); // Recarrega a página após a ativação/inativação
        } else {
            alert(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) cliente(s).`);
        }
    })
    .catch(error => {
        console.error(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) cliente(s):`, error);
    });
}

// Função para verificar se algum checkbox está selecionado e atualizar a visibilidade do botão de inativação
function toggleInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    const selectedClientes = getSelectedClientes();
    inativarButton.style.display = selectedClientes.length > 0 ? 'inline-block' : 'none'; // Mostra/esconde o botão
}

// Função para inicializar a verificação de estado dos checkboxes de clientes
function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedClientes"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleInativarButton);
    });
}

// Função para desabilitar o checkbox "Selecionar todos" se todos os clientes estiverem inativos
function disableSelectAllIfAllInactive() {
    const clienteCheckboxes = document.querySelectorAll('input[name="selectedClientes"]');
    const allInactive = Array.from(clienteCheckboxes).every(checkbox => checkbox.disabled);
    const selectAllCheckbox = document.getElementById('select-all');

    selectAllCheckbox.disabled = allInactive;  // Desabilita o checkbox "Selecionar todos" se todos estiverem inativos
}

// Função para ativar cliente
function ativarCliente(clienteId) {
    if (confirm('Tem certeza que deseja ativar o cliente?')) {
        processClientRequest([clienteId], 'PUT', '/clientes/ativar?idsCliente=');
    }
}
