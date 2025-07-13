import { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  TextField,
  Grid,
  Button
} from "@mui/material";
const ModelForm = ({ open, handleClose, handleSubmit, initialData }) => {
  const payload = {
    topic: "",
    createdBy: ""
  };
  const [formData, setFormData] = useState(payload);

  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    }
  }, [initialData]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const onSubmit = (event) => {
    event.preventDefault(); // Prevent page reload
    handleSubmit(formData); // Pass form data to the parent component
  };

  const onCancel = () => {
    handleClose()
    setFormData(payload)
  }

  return (
    <Dialog open={open} onClose={onCancel}>
      <form onSubmit={onSubmit}>
        <DialogTitle>IoT DEVICE</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new IoT device to the system.
          </DialogContentText>
          <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="topic"
                name="topic"
                label="Topic"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.topic}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="createdBy"
                name="createdBy"
                label="Create by"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.createdBy}
                onChange={handleChange}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={onCancel} variant="outlined" color="error" >Cancel</Button>
          <Button type="submit" variant="outlined" color="primary" >Save</Button>
        </DialogActions>
      </form>
    </Dialog>

  );
};

export default ModelForm;