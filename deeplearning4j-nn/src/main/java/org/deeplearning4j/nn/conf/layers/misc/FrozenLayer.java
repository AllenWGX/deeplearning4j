package org.deeplearning4j.nn.conf.layers.misc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.deeplearning4j.nn.api.ParamInitializer;
import org.deeplearning4j.nn.conf.InputPreProcessor;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.Layer;
import org.deeplearning4j.nn.params.EmptyParamInitializer;
import org.deeplearning4j.optimize.api.IterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.shade.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by Alex on 10/07/2017.
 */
@EqualsAndHashCode
public class FrozenLayer extends Layer {

    @Getter
    protected Layer layer;

    private FrozenLayer(Builder builder){
        super(builder);
        this.layer = builder.layer;
    }

    public FrozenLayer(@JsonProperty("layer") Layer layer){
        this.layer = layer;
    }

    public NeuralNetConfiguration getInnerConf(NeuralNetConfiguration conf){
        NeuralNetConfiguration nnc = conf.clone();
        nnc.setLayer(layer);
        return nnc;
    }

    @Override
    public Layer clone() {
        FrozenLayer l = (FrozenLayer)super.clone();
        l.layer = layer.clone();
        return l;
    }

    @Override
    public org.deeplearning4j.nn.api.Layer instantiate(NeuralNetConfiguration conf, Collection<IterationListener> iterationListeners, int layerIndex, INDArray layerParamsView, boolean initializeParams) {

        //Need to be able to instantiate a layer, from a config - for JSON -> net type situations
        org.deeplearning4j.nn.api.Layer underlying = layer.instantiate(getInnerConf(conf), iterationListeners, layerIndex, layerParamsView, initializeParams);

        return new org.deeplearning4j.nn.layers.FrozenLayer(underlying);
    }

    @Override
    public ParamInitializer initializer() {
        return layer.initializer();
    }

    @Override
    public InputType getOutputType(int layerIndex, InputType inputType) {
        return layer.getOutputType(layerIndex, inputType);
    }

    @Override
    public void setNIn(InputType inputType, boolean override) {
        layer.setNIn(inputType, override);
    }

    @Override
    public InputPreProcessor getPreProcessorForInputType(InputType inputType) {
        return layer.getPreProcessorForInputType(inputType);
    }

    @Override
    public double getL1ByParam(String paramName) {
        return 0;
    }

    @Override
    public double getL2ByParam(String paramName) {
        return 0;
    }

    @Override
    public double getLearningRateByParam(String paramName) {
        return 0;
    }

    @Override
    public boolean isPretrainParam(String paramName) {
        return false;
    }

    @Override
    public Updater getUpdaterByParam(String paramName) {
        return null;
    }

    @Override
    public IUpdater getIUpdaterByParam(String paramName) {
        return null;
    }

    @Override
    public void setLayerName(String layerName){
        super.setLayerName(layerName);
        layer.setLayerName(layerName);
    }

    public static class Builder extends Layer.Builder<Builder>{
        private Layer layer;

        public Builder layer(Layer layer){
            this.layer = layer;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public FrozenLayer build(){
            return new FrozenLayer(this);
        }
    }
}
