import React, { createContext, useContext, useState, useRef, ReactNode } from 'react';

interface CanvasContextType {
  // Drawing state
  selectedTool: string;
  setSelectedTool: (tool: string) => void;
  selectedColor: string;
  setSelectedColor: (color: string) => void;
  brushSize: number;
  setBrushSize: (size: number) => void;
  
  // Canvas operations
  canvasRef: React.RefObject<HTMLCanvasElement> | null;
  isDrawing: boolean;
  setIsDrawing: (drawing: boolean) => void;
  
  // Canvas actions
  clearCanvas: () => void;
  undo: () => void;
  redo: () => void;
  saveCanvas: () => void;
  exportCanvas: (format: 'png' | 'jpg') => void;
}

const CanvasContext = createContext<CanvasContextType | undefined>(undefined);

export const CanvasProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [selectedTool, setSelectedTool] = useState('pen');
  const [selectedColor, setSelectedColor] = useState('#3B82F6');
  const [brushSize, setBrushSize] = useState(2);
  const [isDrawing, setIsDrawing] = useState(false);
  
  const canvasRef = useRef<HTMLCanvasElement>(null);

  const clearCanvas = () => {
    if (!canvasRef.current) return;
    const ctx = canvasRef.current.getContext('2d');
    if (ctx) {
      ctx.clearRect(0, 0, canvasRef.current.width, canvasRef.current.height);
    }
  };

  const undo = () => {
    // Implement undo logic with history stack
    console.log('Undo');
  };

  const redo = () => {
    // Implement redo logic with history stack
    console.log('Redo');
  };

  const saveCanvas = () => {
    if (!canvasRef.current) return;
    const dataUrl = canvasRef.current.toDataURL('image/png');
    console.log('Saving canvas:', dataUrl);
  };

  const exportCanvas = (format: 'png' | 'jpg') => {
    if (!canvasRef.current) return;
    const mimeType = format === 'png' ? 'image/png' : 'image/jpeg';
    const dataUrl = canvasRef.current.toDataURL(mimeType);
    
    const link = document.createElement('a');
    link.download = `canvas-export.${format}`;
    link.href = dataUrl;
    link.click();
  };

  const value: CanvasContextType = {
    selectedTool,
    setSelectedTool,
    selectedColor,
    setSelectedColor,
    brushSize,
    setBrushSize,
    canvasRef,
    isDrawing,
    setIsDrawing,
    clearCanvas,
    undo,
    redo,
    saveCanvas,
    exportCanvas,
  };

  return <CanvasContext.Provider value={value}>{children}</CanvasContext.Provider>;
};

export const useCanvas = (): CanvasContextType => {
  const context = useContext(CanvasContext);
  if (context === undefined) {
    throw new Error('useCanvas must be used within a CanvasProvider');
  }
  return context;
};