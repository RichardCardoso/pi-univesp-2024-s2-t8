window.onload = function() {
    initSelectAllCheckbox('select-all', 'selectedSeguradoras', toggleButtonVisibility);
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive('selectedSeguradoras', 'select-all');
};

function toggleButtonVisibility() {
    toggleInativarButton('inativarSelecionados', getSelectedItems('selectedSeguradoras'));
}

function initInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            const selectedItems = getSelectedItems('selectedSeguradoras');
            if (selectedItems.length > 0) {
                if (confirm('Tem certeza que deseja inativar as seguradoras selecionadas?')) {
                    processRequest(selectedItems, 'DELETE', '/seguradoras?idsSeguradora=',
                                   'Seguradoras inativadas com sucesso.',
                                   'Erro ao inativar a(s) seguradora(s).');
                }
            } else {
                alert('Nenhuma seguradora foi selecionada.');
            }
        };
    }
}

function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedSeguradoras"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleButtonVisibility);
    });
}

// Função para ativar uma seguradora específica
function ativarSeguradora(seguradoraId) {
    if (confirm('Tem certeza que deseja ativar a seguradora?')) {
        processRequest([seguradoraId], 'PUT', '/seguradoras/ativar?idsSeguradora=',
                       'Seguradora ativada com sucesso.',
                       'Erro ao ativar a seguradora.');
    }
}
