import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Typography,
    Chip,
    Paper,
    Divider,
    Button,
    Alert
} from '@mui/material';
import { styled } from '@mui/material/styles';

const JsonPaper = styled(Paper)(({ theme }) => ({
    padding: theme.spacing(2),
    backgroundColor: theme.palette.grey[100],
    fontFamily: 'monospace',
    color: 'black',
    overflowX: 'auto',
    marginTop: theme.spacing(2),
}));

const StatusChip = styled(Chip)(() => ({
    backgroundColor: '#da8300ff',
    color: '#fff',
}));

const StateChip = styled(Chip)(({ state }) => ({
    backgroundColor: state === 'online' || 'on' ? '#81c784' : '#e57373',
    color: '#fff',
}));

const DeviceStatusDialog = ({ open, onClose, message }) => {
    // Safely parse the message
    const parseMessage = () => {
        try {
            if (!message || !message) return null;
            return message
        } catch (error) {
            console.error('Error parsing message:', error);
            return null;
        }
    };




    const data = parseMessage();

    if (!data) {
        return (
            <Dialog open={open} onClose={onClose}>
                <DialogTitle>Error</DialogTitle>
                <DialogContent>
                    <Alert severity="error">
                        No valid device data available or message format is incorrect.
                    </Alert>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Close</Button>
                </DialogActions>
            </Dialog>
        );
    }

    return (
        <Dialog
            open={open}
            onClose={onClose}
            maxWidth="sm"
            fullWidth
            PaperProps={{
                sx: {
                    minHeight: '300px'
                }
            }}
        >
            <DialogTitle>
                Device Status
                <Typography variant="subtitle2" color="text.secondary">
                    Detailed device information
                </Typography>
            </DialogTitle>

            <DialogContent dividers>
                <Typography variant="h6" gutterBottom>Device Information</Typography>

                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '10px' }}>
                    <div>
                        <Typography variant="body2" color="text.secondary">Device</Typography>
                        <Typography>{data?.device || 'N/A'}</Typography>
                    </div>

                    <div>
                        <Typography variant="body2" color="text.secondary">Device ID</Typography>
                        <Typography>{data?.id || 'N/A'}</Typography>
                    </div>

                    <div>
                        <Typography variant="body2" color="text.secondary">Status</Typography>
                        <StatusChip label={data?.status || 'N/A'} />
                    </div>

                    <div>
                        <Typography variant="body2" color="text.secondary">State</Typography>
                        <StateChip label={data?.state || 'N/A'} state={data?.state} />
                    </div>
                    
                    <div>
                        <Typography variant="body2" color="text.secondary">Value</Typography>
                        <Chip label={data?.value || 'N/A'} state={data?.value} />
                    </div>
                </div>

                <Divider sx={{ my: 2 }} />

                <Typography variant="h6" gutterBottom>Raw Data</Typography>
                <JsonPaper elevation={0}>
                    <pre>{JSON.stringify(data, null, 2)}</pre>
                </JsonPaper>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose} color="error">Close</Button>
            </DialogActions>
        </Dialog>
    );
};

export default DeviceStatusDialog;