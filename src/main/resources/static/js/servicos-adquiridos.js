window.onload = function() {
    initSelectAllCheckbox('select-all', 'selectedServicosAdquiridos', toggleButtonVisibility);
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive('selectedServicosAdquiridos', 'select-all');
};

function toggleButtonVisibility() {
    toggleInativarButton('inativarSelecionados', getSelectedItems('selectedServicosAdquiridos'));
}

function initInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            const selectedItems = getSelectedItems('selectedServicosAdquiridos');
            if (selectedItems.length > 0) {
                if (confirm('Tem certeza que deseja inativar os serviços adquiridos selecionados?')) {
                    processRequest(selectedItems, 'DELETE', '/servicos-adquiridos?idsServico=',
                                   '"Serviços adquiridos" inativados com sucesso.',
                                   'Erro ao inativar o(s) serviço(s) adquirido(s).');
                }
            } else {
                alert('Nenhum serviço adquirido foi selecionado.');
            }
        };
    }
}

function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedServicosAdquiridos"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleButtonVisibility);
    });
}

// Função para ativar um serviço adquirido específico
function ativarServico(servicoId) {
    if (confirm('Tem certeza que deseja ativar o "Serviço Adquirido"?')) {
        processRequest([servicoId], 'PUT', '/servicos-adquiridos/ativar?idsServicoAdquirido=',
                       '"Serviço adquirido" ativado com sucesso.',
                       'Erro ao ativar o serviço adquirido.');
    }
}
