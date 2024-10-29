window.onload = function() {
    initSelectAllCheckbox('select-all', 'selectedServicos', toggleButtonVisibility);
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive('selectedServicos', 'select-all');
};

function toggleButtonVisibility() {
    toggleInativarButton('inativarSelecionados', getSelectedItems('selectedServicos'));
}

function initInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            const selectedItems = getSelectedItems('selectedServicos');
            if (selectedItems.length > 0) {
                if (confirm('Tem certeza que deseja inativar os serviços selecionados?')) {
                    processRequest(selectedItems, 'DELETE', '/servicos?idsServico=',
                                   'Serviços inativados com sucesso.',
                                   'Erro ao inativar o(s) serviço(s).');
                }
            } else {
                alert('Nenhum serviço foi selecionado.');
            }
        };
    }
}

function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedServicos"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleButtonVisibility);
    });
}

// Função para ativar um serviço específico
function ativarServico(servicoId) {
    if (confirm('Tem certeza que deseja ativar o serviço?')) {
        processRequest([servicoId], 'PUT', '/servicos/ativar?idsServico=',
                       'Serviço ativado com sucesso.',
                       'Erro ao ativar o serviço.');
    }
}
