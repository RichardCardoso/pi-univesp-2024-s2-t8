window.onload = function() {
    initSelectAllCheckbox('select-all', 'selectedClientes', toggleButtonVisibility);
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive('selectedClientes', 'select-all');
};

function toggleButtonVisibility() {
    toggleInativarButton('inativarSelecionados', getSelectedItems('selectedClientes'));
}

function initInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            const selectedItems = getSelectedItems('selectedClientes');
            if (selectedItems.length > 0) {
                if (confirm('Tem certeza que deseja inativar os clientes selecionados?')) {
                    processRequest(selectedItems, 'DELETE', '/clientes?idsCliente=',
                                   'Clientes inativados com sucesso.',
                                   'Erro ao inativar o(s) cliente(s).');
                }
            } else {
                alert('Nenhum cliente foi selecionado.');
            }
        };
    }
}

function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedClientes"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleButtonVisibility);
    });
}

// Função para ativar um cliente específico
function ativarCliente(clienteId) {
    if (confirm('Tem certeza que deseja ativar o cliente?')) {
        processRequest([clienteId], 'PUT', '/clientes/ativar?idsCliente=',
                       'Cliente ativado com sucesso.',
                       'Erro ao ativar o cliente.');
    }
}
